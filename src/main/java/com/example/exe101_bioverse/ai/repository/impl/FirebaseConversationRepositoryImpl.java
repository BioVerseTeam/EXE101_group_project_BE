package com.example.exe101_bioverse.ai.repository.impl;

import com.example.exe101_bioverse.ai.dto.ChatMessage;
import com.example.exe101_bioverse.ai.repository.AiConversationRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FirebaseConversationRepositoryImpl implements AiConversationRepository {

    private final Firestore firestore;

    @Value("${bioverse.ai.model}")
    private String aiModel;

    public FirebaseConversationRepositoryImpl(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public String createOrGetConversation(String conversationId, String studentId, String firstQuestion) {
        try {
            String finalConversationId = conversationId;
            if (finalConversationId == null || finalConversationId.trim().isEmpty()) {
                finalConversationId = UUID.randomUUID().toString();
            }

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
            throw new RuntimeException("Error interacting with Firestore (createOrGetConversation): " + e.getMessage(), e);
        }
    }

    @Override
    public void saveMessage(String conversationId, String role, String content) {
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
            throw new RuntimeException("Error interacting with Firestore (saveMessage): " + e.getMessage(), e);
        }
    }

    @Override
    public List<ChatMessage> getRecentMessages(String conversationId, int limit) {
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
            throw new RuntimeException("Error interacting with Firestore (getRecentMessages): " + e.getMessage(), e);
        }
    }
}
