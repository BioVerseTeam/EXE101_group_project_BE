package com.example.exe101_bioverse.storage.service;

import com.example.exe101_bioverse.storage.dto.ModelAssetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

public interface R2StorageService {

    List<ModelAssetResponse> listModels();

    ResponseEntity<StreamingResponseBody> streamModel(String objectKey);

    ModelAssetResponse uploadObject(String objectKey, byte[] bytes, String contentType);
}
