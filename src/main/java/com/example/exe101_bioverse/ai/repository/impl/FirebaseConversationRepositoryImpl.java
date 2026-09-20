package com.example.exe101_bioverse.ai.repository.impl;

import com.example.exe101_bioverse.ai.dto.ChatMessage;
import com.example.exe101_bioverse.ai.repository.AiConversationRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class FirebaseConversationRepositoryImpl implements AiConversationRepository {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConversationRepositoryImpl.class);

    private final Firestore firestore;

    @Value("${bioverse.ai.model}")
    private String aiModel;

    // In-memory fallback khi Firebase chưa được cấu hình
    private final Map<String, List<ChatMessage>> inMemoryMessages = new ConcurrentHashMap<>();
    private final Map<String, String> inMemoryTitles = new ConcurrentHashMap<>();

    public FirebaseConversationRepositoryImpl(@Autowired(required = false) Firestore firestore) {
        this.firestore = firestore;
        if (this.firestore == null) {
            log.warn("⚠️ Firebase Firestore không khả dụng. Hệ thống AI Conversation chuyển sang chế độ fallback IN-MEMORY.");
        }
    }

    @Override
    public String createOrGetConversation(String conversationId, String studentId, String firstQuestion) {
        String finalConversationId = conversationId;
        if (finalConversationId == null || finalConversationId.trim().isEmpty()) {
            finalConversationId = UUID.randomUUID().toString();
        }

        if (firestore == null) {
            inMemoryMessages.putIfAbsent(finalConversationId, new CopyOnWriteArrayList<>());
            String title = firstQuestion != null ? firstQuestion.substring(0, Math.min(firstQuestion.length(), 50)) : "Cuộc trò chuyện";
            if (firstQuestion != null && firstQuestion.length() > 50) {
                title += "...";
            }
            inMemoryTitles.putIfAbsent(finalConversationId, title);
            return finalConversationId;
        }

        try {
            DocumentReference convRef = firestore.collection("ai_conversations").document(finalConversationId);
            ApiFuture<DocumentSnapshot> snapshotFuture = convRef.get();
            DocumentSnapshot snapshot = snapshotFuture.get();

            if (!snapshot.exists()) {
                // Create new conversation document
                Map<String, Object> data = new HashMap<>();
                data.put("studentId", studentId);
                
                // Limit title length to 50 characters
                String title = firstQuestion.substring(0, Math.min(firstQuestion.length(), 50));
                if (firstQuestion.length() > 50) {
                    title += "...";
                }
                data.put("title", title);
                data.put("model", aiModel);
                data.put("createdAt", FieldValue.serverTimestamp());
                data.put("updatedAt", FieldValue.serverTimestamp());
                convRef.set(data).get();
            } else {
                // Update conversation updatedAt
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
        if (firestore == null) {
            inMemoryMessages.computeIfAbsent(conversationId, k -> new CopyOnWriteArrayList<>())
                    .add(new ChatMessage(role, content));
            return;
        }

        try {
            DocumentReference convRef = firestore.collection("ai_conversations").document(conversationId);
            
            // Also update parent conversation's updatedAt
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
                    .add(new ChatMessage(role, content));
        }
    }

    @Override
    public List<ChatMessage> getRecentMessages(String conversationId, int limit) {
        if (firestore == null) {
            List<ChatMessage> list = inMemoryMessages.getOrDefault(conversationId, Collections.emptyList());
            int size = list.size();
            if (size <= limit) {
                return new ArrayList<>(list);
            }
            return new ArrayList<>(list.subList(size - limit, size));
        }

        try {
            DocumentReference convRef = firestore.collection("ai_conversations").document(conversationId);
            CollectionReference messagesRef = convRef.collection("messages");

            // Query messages ordered by createdAt DESC to get the latest messages up to limit
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

            // The query returned messages ordered DESC, so we reverse it to chronological order (old -> new)
            Collections.reverse(messages);
            return messages;
        } catch (Exception e) {
            log.warn("Lỗi tương tác Firestore (getRecentMessages): {}. Tự động fallback đọc in-memory.", e.getMessage());
            List<ChatMessage> list = inMemoryMessages.getOrDefault(conversationId, Collections.emptyList());
            int size = list.size();
            if (size <= limit) {
                return new ArrayList<>(list);
            }
            return new ArrayList<>(list.subList(size - limit, size));
        }
    }
}
