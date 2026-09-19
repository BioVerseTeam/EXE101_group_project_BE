package com.example.exe101_bioverse.storage.controller;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.common.response.ApiResponse;
import com.example.exe101_bioverse.storage.dto.ModelAssetResponse;
import com.example.exe101_bioverse.storage.service.R2StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RestController
@RequestMapping("/api/models")
@Tag(name = "3D Models", description = "Proxy 3D assets from Cloudflare R2")
@SecurityRequirements
public class ModelAssetController {

    private final R2StorageService r2StorageService;

    public ModelAssetController(R2StorageService r2StorageService) {
        this.r2StorageService = r2StorageService;
    }

    @GetMapping({ "", "/", "/assets" })
    @Operation(summary = "List 3D model files stored in R2")
    public ResponseEntity<ApiResponse<List<ModelAssetResponse>>> listModels() {
        return ResponseEntity.ok(ApiResponse.success(r2StorageService.listModels()));
    }

    @RequestMapping(value = "/{*objectKey}", method = {RequestMethod.GET, RequestMethod.HEAD})
    @Operation(summary = "Download a 3D model file through the backend")
    public ResponseEntity<StreamingResponseBody> getModel(@PathVariable String objectKey) {
        if (isReservedKey(objectKey)) {
            throw new AppException(ErrorCode.MODEL_NOT_FOUND);
        }
        return r2StorageService.streamModel(objectKey);
    }

    private boolean isReservedKey(String objectKey) {
        if (objectKey == null) return true;
        String first = objectKey.replaceFirst("^/+", "").split("/")[0].toLowerCase();
        return first.equals("assets")
                || first.equals("catalog")
                || first.equals("categories")
                || first.equals("featured")
                || first.equals("labs")
                || first.equals("detail")
                || first.equals("slug");
    }
}
