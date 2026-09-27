package com.example.exe101_bioverse.exam.controller;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.exam.dto.response.MediaUploadResponse;
import com.example.exe101_bioverse.storage.dto.ModelAssetResponse;
import com.example.exe101_bioverse.storage.service.R2StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/media")
@Validated
public class MediaController {

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp"
    );

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Autowired
    private R2StorageService r2StorageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<MediaUploadResponse>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false, defaultValue = "exams") String folder) {

        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_FILE, "Vui lòng chọn file hình ảnh hợp lệ");
        }

        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        if (!ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new AppException(ErrorCode.INVALID_FILE, "Định dạng file không được hỗ trợ. Chỉ chấp nhận JPG, PNG hoặc WebP");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE, "Dung lượng ảnh vượt quá giới hạn cho phép (tối đa 5MB)");
        }

        String ext = contentType.contains("png") ? "png" : contentType.contains("webp") ? "webp" : "jpg";
        String cleanFolder = folder.replaceAll("[^a-zA-Z0-9/_-]", "");
        if (cleanFolder.isBlank()) {
            cleanFolder = "exams";
        }
        String key = cleanFolder + "/" + UUID.randomUUID() + "." + ext;

        try {
            ModelAssetResponse uploaded = r2StorageService.uploadObject(key, file.getBytes(), contentType);

            MediaUploadResponse response = MediaUploadResponse.builder()
                    .url(uploaded.url())
                    .fileName(file.getOriginalFilename() != null ? file.getOriginalFilename() : key)
                    .fileSize(file.getSize())
                    .mimeType(contentType)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(response, "Upload hình ảnh thành công"));
        } catch (IOException e) {
            throw new AppException(ErrorCode.STORAGE_ERROR, "Không thể đọc dữ liệu file để upload");
        }
    }
}
