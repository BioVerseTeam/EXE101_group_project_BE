package com.example.exe101_bioverse.ai.repository.impl;

import com.example.exe101_bioverse.ai.dto.ChatMessage;
import com.example.exe101_bioverse.ai.dto.ChatMessageResponse;
import com.example.exe101_bioverse.ai.dto.ConversationSummaryResponse;
import com.example.exe101_bioverse.ai.dto.PagedResponse;
import com.example.exe101_bioverse.ai.repository.AiConversationRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class FirebaseConversationRepositoryImpl implements AiConversationRepository {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConversationRepositoryImpl.class);

    private final Firestore firestore;

    @Value("${bioverse.ai.model:openai/gpt-4o-mini}")
    private String aiModel;

    // In-memory fallback structures for development when Firestore is unavailable
    private final Map<String, List<ChatMessageRecord>> inMemoryMessages = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> inMemoryMetadata = new ConcurrentHashMap<>();

    private record ChatMessageRecord(String role, String content, Instant createdAt) {}

    public FirebaseConversationRepositoryImpl(ObjectProvider<Firestore> firestoreProvider) {
        this.firestore = firestoreProvider.getIfAvailable();
        if (this.firestore == null) {
            log.warn("⚠️ Firebase Firestore không khả dụng. Hệ thống AI Conversation chuyển sang chế độ fallback IN-MEMORY.");
        }
    }

    @Override
    public String createOrGetConversation(String conversationId, String userId, String studentId, String firstQuestion) {
        String finalConversationId = conversationId;
        if (finalConversationId == null || finalConversationId.trim().isEmpty()) {
            finalConversationId = UUID.randomUUID().toString();
        }

        String title = firstQuestion != null ? firstQuestion.substring(0, Math.min(firstQuestion.length(), 50)) : "Cuộc trò chuyện";
        if (firstQuestion != null && firstQuestion.length() > 50) {
            title += "...";
        }

        final String targetConvId = finalConversationId;
        final String finalUserId = userId != null ? userId : "";
        final String finalStudentId = userId == null && studentId != null ? studentId : "";
        final String finalTitle = title;
        if (firestore == null) {
            inMemoryMessages.putIfAbsent(targetConvId, new CopyOnWriteArrayList<>());
            Map<String, Object> meta = inMemoryMetadata.computeIfAbsent(targetConvId, k -> {
                Map<String, Object> m = new ConcurrentHashMap<>();
                m.put("conversationId", targetConvId);
                m.put("userId", finalUserId);
                m.put("studentId", finalStudentId);
                m.put("title", finalTitle);
                m.put("model", aiModel);
                m.put("createdAt", Instant.now());
                m.put("updatedAt", Instant.now());
                return m;
            });
            meta.put("updatedAt", Instant.now());
            return finalConversationId;
        }

        try {
            DocumentReference convRef = firestore.collection("ai_conversations").document(finalConversationId);
            ApiFuture<DocumentSnapshot> snapshotFuture = convRef.get();
            DocumentSnapshot snapshot = snapshotFuture.get();

            if (!snapshot.exists()) {
                Map<String, Object> data = new HashMap<>();
                if (userId != null && !userId.trim().isEmpty()) {
                    data.put("userId", userId);
                    data.put("studentId", null);
                } else {
                    data.put("userId", null);
                    data.put("studentId", studentId);
                }
                data.put("title", title);
                data.put("model", aiModel);
                data.put("createdAt", FieldValue.serverTimestamp());
                data.put("updatedAt", FieldValue.serverTimestamp());
                convRef.set(data).get();
            } else {
                convRef.update("updatedAt", FieldValue.serverTimestamp()).get();
            }

            return finalConversationId;
        } catch (Exception e) {
            log.warn("Lỗi tương tác Firestore (createOrGetConversation): {}. Tự động fallback lưu in-memory.", e.getMessage());
            inMemoryMessages.putIfAbsent(finalConversationId, new CopyOnWriteArrayList<>());
            return finalConversationId;
        }
    }

    @Override
    public void saveMessage(String conversationId, String role, String content) {
        Instant now = Instant.now();
        if (firestore == null) {
            inMemoryMessages.computeIfAbsent(conversationId, k -> new CopyOnWriteArrayList<>())
                    .add(new ChatMessageRecord(role, content, now));
            Map<String, Object> meta = inMemoryMetadata.get(conversationId);
            if (meta != null) {
                meta.put("updatedAt", now);
            }
            return;
        }

        try {
            DocumentReference convRef = firestore.collection("ai_conversations").document(conversationId);
            convRef.update("updatedAt", FieldValue.serverTimestamp());

            CollectionReference messagesRef = convRef.collection("messages");
            Map<String, Object> msgData = new HashMap<>();
            msgData.put("role", role);
            msgData.put("content", content);
            msgData.put("createdAt", FieldValue.serverTimestamp());

            messagesRef.add(msgData).get();
        } catch (Exception e) {
            log.warn("Lỗi tương tác Firestore (saveMessage): {}. Tự động fallback lưu in-memory.", e.getMessage());
            inMemoryMessages.computeIfAbsent(conversationId, k -> new CopyOnWriteArrayList<>())
                    .add(new ChatMessageRecord(role, content, now));
        }
    }

    @Override
    public List<ChatMessage> getRecentMessages(String conversationId, int limit) {
        if (firestore == null) {
            List<ChatMessageRecord> list = inMemoryMessages.getOrDefault(conversationId, Collections.emptyList());
            int size = list.size();
            List<ChatMessage> result = new ArrayList<>();
            List<ChatMessageRecord> subList = size <= limit ? list : list.subList(size - limit, size);
            for (ChatMessageRecord r : subList) {
                result.add(new ChatMessage(r.role(), r.content()));
            }
            return result;
        }

        try {
            DocumentReference convRef = firestore.collection("ai_conversations").document(conversationId);
            CollectionReference messagesRef = convRef.collection("messages");

            Query query = messagesRef.orderBy("createdAt", Query.Direction.DESCENDING).limit(limit);
            ApiFuture<QuerySnapshot> querySnapshotFuture = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshotFuture.get().getDocuments();

            List<ChatMessage> messages = new ArrayList<>();
            for (QueryDocumentSnapshot doc : documents) {
                messages.add(new ChatMessage(
                        doc.getString("role"),
                        doc.getString("content")
                ));
            }

            Collections.reverse(messages);
            return messages;
        } catch (Exception e) {
            log.warn("Lỗi tương tác Firestore (getRecentMessages): {}. Tự động fallback đọc in-memory.", e.getMessage());
            List<ChatMessageRecord> list = inMemoryMessages.getOrDefault(conversationId, Collections.emptyList());
            int size = list.size();
            List<ChatMessage> result = new ArrayList<>();
            List<ChatMessageRecord> subList = size <= limit ? list : list.subList(size - limit, size);
            for (ChatMessageRecord r : subList) {
                result.add(new ChatMessage(r.role(), r.content()));
            }
            return result;
        }
    }

    @Override
    public Map<String, Object> getConversationDocument(String conversationId) {
        if (conversationId == null || conversationId.trim().isEmpty()) {
            return null;
        }

        if (firestore == null) {
            return inMemoryMetadata.get(conversationId);
        }

        try {
            DocumentReference convRef = firestore.collection("ai_conversations").document(conversationId);
            DocumentSnapshot snapshot = convRef.get().get();
            if (!snapshot.exists()) {
                return null;
            }
            Map<String, Object> data = snapshot.getData();
            if (data == null) data = new HashMap<>();
            data.put("id", snapshot.getId());
            return data;
        } catch (Exception e) {
            log.warn("Lỗi tương tác Firestore (getConversationDocument): {}", e.getMessage());
            return inMemoryMetadata.get(conversationId);
        }
    }

    @Override
    public PagedResponse<ConversationSummaryResponse> getUserConversations(String userId, int limit, String cursor) {
        int safeLimit = Math.min(Math.max(limit, 1), 50);

        if (firestore == null) {
            List<ConversationSummaryResponse> all = new ArrayList<>();
            for (Map<String, Object> meta : inMemoryMetadata.values()) {
                String metaUserId = (String) meta.get("userId");
                if (userId != null && userId.equals(metaUserId)) {
                    Instant cAt = (Instant) meta.getOrDefault("createdAt", Instant.now());
                    Instant uAt = (Instant) meta.getOrDefault("updatedAt", Instant.now());
                    all.add(new ConversationSummaryResponse(
                            (String) meta.get("conversationId"),
                            (String) meta.getOrDefault("title", "Cuộc trò chuyện"),
                            cAt,
                            uAt
                    ));
                }
            }
            all.sort((a, b) -> b.updatedAt().compareTo(a.updatedAt()));
            int total = all.size();
            int startIndex = 0;
            if (cursor != null && !cursor.isBlank()) {
                for (int i = 0; i < total; i++) {
                    if (all.get(i).conversationId().equals(cursor)) {
                        startIndex = i + 1;
                        break;
                    }
                }
            }
            int endIndex = Math.min(startIndex + safeLimit, total);
            List<ConversationSummaryResponse> pageItems = startIndex < total ? all.subList(startIndex, endIndex) : Collections.emptyList();
            boolean hasMore = endIndex < total;
            String nextCursor = (hasMore && !pageItems.isEmpty()) ? pageItems.get(pageItems.size() - 1).conversationId() : null;
            return new PagedResponse<>(pageItems, nextCursor, hasMore);
        }

        try {
            Query query = firestore.collection("ai_conversations")
                    .whereEqualTo("userId", userId);

            ApiFuture<QuerySnapshot> querySnapshotFuture = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshotFuture.get().getDocuments();

            List<ConversationSummaryResponse> allItems = new ArrayList<>();
            for (QueryDocumentSnapshot doc : documents) {
                Timestamp cAt = doc.getTimestamp("createdAt");
                Timestamp uAt = doc.getTimestamp("updatedAt");
                Instant cInstant = cAt != null ? Instant.ofEpochSecond(cAt.getSeconds(), cAt.getNanos()) : Instant.now();
                Instant uInstant = uAt != null ? Instant.ofEpochSecond(uAt.getSeconds(), uAt.getNanos()) : Instant.now();
                String title = doc.getString("title");
                if (title == null || title.trim().isEmpty()) {
                    title = "Cuộc trò chuyện";
                }
                allItems.add(new ConversationSummaryResponse(
                        doc.getId(),
                        title,
                        cInstant,
                        uInstant
                ));
            }

            // Sort in memory deterministically: updatedAt DESC, documentId DESC
            allItems.sort((a, b) -> {
                int cmp = b.updatedAt().compareTo(a.updatedAt());
                if (cmp != 0) return cmp;
                return b.conversationId().compareTo(a.conversationId());
            });

            int total = allItems.size();
            int startIndex = 0;
            if (cursor != null && !cursor.isBlank()) {
                for (int i = 0; i < total; i++) {
                    if (allItems.get(i).conversationId().equals(cursor)) {
                        startIndex = i + 1;
                        break;
                    }
                }
            }

            int endIndex = Math.min(startIndex + safeLimit, total);
            List<ConversationSummaryResponse> pageItems = startIndex < total ? allItems.subList(startIndex, endIndex) : Collections.emptyList();
            boolean hasMore = endIndex < total;
            String nextCursor = (hasMore && !pageItems.isEmpty()) ? pageItems.get(pageItems.size() - 1).conversationId() : null;

            return new PagedResponse<>(pageItems, nextCursor, hasMore);
        } catch (Exception e) {
            log.warn("Lỗi tương tác Firestore (getUserConversations): {}. Fallback in-memory.", e.getMessage());
            return new PagedResponse<>(Collections.emptyList(), null, false);
        }
    }

    @Override
    public List<ChatMessageResponse> getConversationMessages(String conversationId, int limit) {
        int safeLimit = Math.min(Math.max(limit, 1), 100);

        if (firestore == null) {
            List<ChatMessageRecord> list = inMemoryMessages.getOrDefault(conversationId, Collections.emptyList());
            int size = list.size();
            List<ChatMessageRecord> subList = size <= safeLimit ? list : list.subList(size - safeLimit, size);
            List<ChatMessageResponse> result = new ArrayList<>();
            for (ChatMessageRecord r : subList) {
                result.add(new ChatMessageResponse(r.role(), r.content(), r.createdAt()));
            }
            return result;
        }

        try {
            DocumentReference convRef = firestore.collection("ai_conversations").document(conversationId);
            CollectionReference messagesRef = convRef.collection("messages");

            Query query = messagesRef.orderBy("createdAt", Query.Direction.DESCENDING).limit(safeLimit);
            ApiFuture<QuerySnapshot> querySnapshotFuture = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshotFuture.get().getDocuments();

            List<ChatMessageResponse> messages = new ArrayList<>();
            for (QueryDocumentSnapshot doc : documents) {
                Timestamp cAt = doc.getTimestamp("createdAt");
                Instant cInstant = cAt != null ? Instant.ofEpochSecond(cAt.getSeconds(), cAt.getNanos()) : Instant.now();
                messages.add(new ChatMessageResponse(
                        doc.getString("role"),
                        doc.getString("content"),
                        cInstant
                ));
            }

            Collections.reverse(messages);
            return messages;
        } catch (Exception e) {
            log.warn("Lỗi tương tác Firestore (getConversationMessages): {}. Fallback in-memory.", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public void deleteConversation(String conversationId) {
        if (conversationId == null || conversationId.trim().isEmpty()) {
            return;
        }

        if (firestore == null) {
            inMemoryMessages.remove(conversationId);
            inMemoryMetadata.remove(conversationId);
            return;
        }

        try {
            DocumentReference convRef = firestore.collection("ai_conversations").document(conversationId);
            CollectionReference messagesRef = convRef.collection("messages");

            boolean hasMore = true;
            while (hasMore) {
                QuerySnapshot snapshot = messagesRef.limit(100).get().get();
                if (snapshot.isEmpty()) {
                    hasMore = false;
                } else {
                    WriteBatch batch = firestore.batch();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        batch.delete(doc.getReference());
                    }
                    batch.commit().get();
                }
            }

            convRef.delete().get();
        } catch (Exception e) {
            log.warn("Lỗi tương tác Firestore (deleteConversation): {}", e.getMessage());
            inMemoryMessages.remove(conversationId);
            inMemoryMetadata.remove(conversationId);
        }
    }
}
