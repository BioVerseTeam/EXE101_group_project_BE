package com.example.exe101_bioverse.model.service;

import com.example.exe101_bioverse.model.dto.request.CreateModelRequest;
import com.example.exe101_bioverse.model.dto.request.ToggleFeaturedRequest;
import com.example.exe101_bioverse.model.dto.request.UpdateModelRequest;
import com.example.exe101_bioverse.model.dto.response.ModelDetailResponse;
import com.example.exe101_bioverse.common.response.PageResponse;

/**
 * Service quản trị dành cho Admin - CRUD + toggle featured.
 */
public interface AdminBioModelService {

    /** Admin: Danh sách model với bộ lọc (bao gồm inactive). */
    PageResponse<ModelDetailResponse> listModels(String q, Boolean isFeatured, Boolean isActive, int page, int size);

    /** Admin: Xem chi tiết model (bao gồm inactive). */
    ModelDetailResponse getModel(Long id);

    /** Admin: Tạo model mới. */
    ModelDetailResponse createModel(CreateModelRequest request);

    /** Admin: Cập nhật model. */
    ModelDetailResponse updateModel(Long id, UpdateModelRequest request);

    /** Admin: Bật/tắt cờ featured và thứ tự hiển thị. */
    ModelDetailResponse toggleFeatured(Long id, ToggleFeaturedRequest request);

    /** Admin: Xóa model (soft delete - chuyển isActive = false). */
    void deleteModel(Long id);
}
