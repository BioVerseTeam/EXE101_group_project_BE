package com.example.exe101_bioverse.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${bioverse.firebase.service-account-path}")
    private String serviceAccountPath;

    @Bean
    public Firestore firestore() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream serviceAccountStream;

            // Try reading from file system first
            File file = new File(serviceAccountPath);
            if (file.exists()) {
                serviceAccountStream = new FileInputStream(file);
            } else {
                // Fallback to classpath if packaged inside jar
                serviceAccountStream = getClass().getClassLoader().getResourceAsStream(serviceAccountPath);
                if (serviceAccountStream == null) {
                    throw new FileNotFoundException("Firebase service account key file not found at: " 
                            + file.getAbsolutePath() + " or in classpath: " + serviceAccountPath 
                            + ". Please make sure you have placed 'serviceAccountKey.json' in 'firebase/' directory at the project root.");
                }
            }

            try (serviceAccountStream) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                        .build();

                FirebaseApp.initializeApp(options);
            }
        }

        return FirestoreClient.getFirestore();
    }
}
