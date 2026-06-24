package com.example.exe101_bioverse.ai.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseConversationService {

    private final Firestore firestore;

    @Value("${bioverse.ai.model}")
    private String aiModel;

    @Value("${bioverse.ai.max-history-messages}")
    private int maxHistoryMessages;

    public FirebaseConversationService(Firestore firestore) {
        this.firestore = firestore;
    }

    /**
     * Creates or updates a conversation metadata and returns its ID.
     */
    public String createOrGetConversation(String conversationId, String studentId, String firstQuestion) throws ExecutionException, InterruptedException {
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
    }

    /**
     * Saves a message to the conversation subcollection.
     */
    public void saveMessage(String conversationId, String role, String content) throws ExecutionException, InterruptedException {
        DocumentReference convRef = firestore.collection("ai_conversations").document(conversationId);
        
        // Also update parent conversation's updatedAt
        convRef.update("updatedAt", FieldValue.serverTimestamp());

        CollectionReference messagesRef = convRef.collection("messages");

        Map<String, Object> msgData = new HashMap<>();
        msgData.put("role", role);
        msgData.put("content", content);
        msgData.put("createdAt", FieldValue.serverTimestamp());

        messagesRef.add(msgData).get();
    }

    /**
     * Retrieves recent messages for a conversation, ordered chronologically (old -> new).
     */
    public List<Map<String, String>> getRecentMessages(String conversationId) throws ExecutionException, InterruptedException {
        DocumentReference convRef = firestore.collection("ai_conversations").document(conversationId);
        CollectionReference messagesRef = convRef.collection("messages");

        // Query messages ordered by createdAt DESC to get the latest messages up to maxHistoryMessages
        Query query = messagesRef.orderBy("createdAt", Query.Direction.DESCENDING).limit(maxHistoryMessages);
        ApiFuture<QuerySnapshot> querySnapshotFuture = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshotFuture.get().getDocuments();

        List<Map<String, String>> messages = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            Map<String, String> msg = new HashMap<>();
            msg.put("role", doc.getString("role"));
            msg.put("content", doc.getString("content"));
            messages.add(msg);
        }

        // The query returned messages ordered DESC, so we reverse it to chronological order (old -> new)
        Collections.reverse(messages);
        return messages;
    }
}
