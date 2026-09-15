package com.example.exe101_bioverse.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class FirebaseConfig {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${bioverse.firebase.service-account-path}")
    private String serviceAccountPath;

    @Bean
    public Firestore firestore() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            File keyFile = findServiceAccountFile();
            InputStream serviceAccountStream;
            if (keyFile != null) {
                log.info("Firebase service account loaded from {}", keyFile.getAbsolutePath());
                serviceAccountStream = new FileInputStream(keyFile);
            } else {
                serviceAccountStream = getClass().getClassLoader().getResourceAsStream(serviceAccountPath);
                if (serviceAccountStream == null) {
                    throw new FileNotFoundException(
                            "Firebase service account key not found. Tried: " + String.join(", ", candidatePaths())
                                    + " and classpath:" + serviceAccountPath
                    );
                }
                log.info("Firebase service account loaded from classpath:{}", serviceAccountPath);
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

    private File findServiceAccountFile() {
        for (String candidate : candidatePaths()) {
            File file = new File(candidate);
            if (file.isFile()) {
                return file;
            }
        }
        return null;
    }

    private List<String> candidatePaths() {
        Path cwd = Path.of(System.getProperty("user.dir", ".")).toAbsolutePath().normalize();
        List<String> paths = new ArrayList<>();
        paths.add(serviceAccountPath);
        paths.add(cwd.resolve(serviceAccountPath).toString());
        paths.add(cwd.resolve("EXE101_group_project_BE").resolve(serviceAccountPath).toString());
        paths.add(cwd.resolve("Backend/EXE101_group_project_BE").resolve(serviceAccountPath).toString());

        Path current = cwd;
        for (int i = 0; i < 5 && current != null; i++) {
            paths.add(current.resolve("Backend/EXE101_group_project_BE").resolve(serviceAccountPath).toString());
            paths.add(current.resolve("EXE101_group_project_BE").resolve(serviceAccountPath).toString());
            paths.add(current.resolve(serviceAccountPath).toString());
            current = current.getParent();
        }
        return paths.stream().distinct().toList();
    }
}
