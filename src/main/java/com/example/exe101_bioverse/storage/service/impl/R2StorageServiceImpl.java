package com.example.exe101_bioverse.storage.service.impl;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.storage.dto.ModelAssetResponse;
import com.example.exe101_bioverse.storage.service.R2StorageService;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.util.UriUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class R2StorageServiceImpl implements R2StorageService {

    private static final Logger log = LoggerFactory.getLogger(R2StorageServiceImpl.class);

    private final S3Client s3Client;
    private final String bucket;

    public R2StorageServiceImpl(
            @Value("${bioverse.r2.account-id:}") String accountId,
            @Value("${bioverse.r2.access-key-id:}") String accessKeyId,
            @Value("${bioverse.r2.secret-access-key:}") String secretAccessKey,
            @Value("${bioverse.r2.bucket-name:bio3d-models}") String bucketName,
            @Value("${bioverse.r2.endpoint:}") String endpoint
    ) {
        this.bucket = bucketName;
        if (isBlank(accountId) || isBlank(accessKeyId) || isBlank(secretAccessKey)) {
            log.warn("R2 is not configured. Set R2_ACCOUNT_ID, R2_ACCESS_KEY_ID, R2_SECRET_ACCESS_KEY in .env");
            this.s3Client = null;
            return;
        }

        String resolvedEndpoint = isBlank(endpoint)
                ? "https://" + accountId + ".r2.cloudflarestorage.com"
                : endpoint;

        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(resolvedEndpoint))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                ))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .chunkedEncodingEnabled(false)
                        .build())
                .build();
        log.info("R2 client ready for bucket {}", bucketName);
    }

    @Override
    public List<ModelAssetResponse> listModels() {
        if (s3Client == null) {
            return List.of();
        }
        try {
            List<ModelAssetResponse> models = new ArrayList<>();
            var request = ListObjectsV2Request.builder().bucket(bucket).build();
            s3Client.listObjectsV2Paginator(request).stream()
                    .flatMap(page -> page.contents().stream())
                    .filter(object -> object.key() != null && !object.key().endsWith("/"))
                    .forEach(object -> models.add(new ModelAssetResponse(
                            object.key(),
                            object.size(),
                            "/api/models/" + UriUtils.encodePath(object.key(), StandardCharsets.UTF_8)
                    )));
            return models;
        } catch (S3Exception ex) {
            log.error("Failed to list R2 objects: {}", ex.getMessage());
            return List.of();
        } catch (RuntimeException ex) {
            log.error("Failed to list R2 objects: {}", ex.getMessage());
            return List.of();
        }
    }

    @Override
    public ResponseEntity<StreamingResponseBody> streamModel(String objectKey) {
        String key = normalizeKey(objectKey);
        S3Client client = requireClient();

        ResponseInputStream<GetObjectResponse> object;
        try {
            object = client.getObject(GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        } catch (NoSuchKeyException ex) {
            throw new AppException(ErrorCode.MODEL_NOT_FOUND);
        } catch (S3Exception ex) {
            if (ex.statusCode() == 404) {
                throw new AppException(ErrorCode.MODEL_NOT_FOUND);
            }
            log.error("Failed to fetch R2 object {}: {}", key, ex.getMessage());
            throw new AppException(ErrorCode.STORAGE_ERROR);
        }

        GetObjectResponse metadata = object.response();
        MediaType mediaType = resolveMediaType(key, metadata.contentType());
        long contentLength = metadata.contentLength() != null ? metadata.contentLength() : -1L;

        StreamingResponseBody body = outputStream -> {
            try (object) {
                object.transferTo(outputStream);
            }
        };

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName(key) + "\"");
        if (contentLength >= 0) {
            builder.contentLength(contentLength);
        }
        return builder.body(body);
    }

    @Override
    public ModelAssetResponse uploadObject(String objectKey, byte[] bytes, String contentType) {
        String key = normalizeKey(objectKey);
        S3Client client = requireClient();
        try {
            client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(contentType)
                            .contentLength((long) bytes.length)
                            .build(),
                    RequestBody.fromBytes(bytes)
            );
        } catch (S3Exception ex) {
            log.error("Failed to upload R2 object {}: {}", key, ex.getMessage());
            throw new AppException(ErrorCode.STORAGE_ERROR);
        }
        return new ModelAssetResponse(
                key,
                (long) bytes.length,
                "/api/models/" + UriUtils.encodePath(key, StandardCharsets.UTF_8)
        );
    }

    @PreDestroy
    void close() {
        if (s3Client != null) {
            s3Client.close();
        }
    }

    private S3Client requireClient() {
        if (s3Client == null) {
            throw new AppException(ErrorCode.STORAGE_NOT_CONFIGURED);
        }
        return s3Client;
    }

    private String normalizeKey(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new AppException(ErrorCode.MODEL_NOT_FOUND);
        }
        String key = raw.startsWith("/") ? raw.substring(1) : raw;
        if (key.isBlank() || key.contains("..") || key.startsWith("\\")) {
            throw new AppException(ErrorCode.INVALID_DATA, "Object key không hợp lệ");
        }
        return key;
    }

    private MediaType resolveMediaType(String key, String storedType) {
        if (!isBlank(storedType) && !"application/octet-stream".equalsIgnoreCase(storedType)) {
            try {
                return MediaType.parseMediaType(storedType);
            } catch (Exception ignored) {
                // fall through to extension mapping
            }
        }
        String lower = key.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".glb")) {
            return MediaType.parseMediaType("model/gltf-binary");
        }
        if (lower.endsWith(".gltf")) {
            return MediaType.parseMediaType("model/gltf+json");
        }
        if (lower.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        }
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    private String fileName(String key) {
        int slash = key.lastIndexOf('/');
        return slash >= 0 ? key.substring(slash + 1) : key;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
