package com.example.exe101_bioverse.model.service;

import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.model.dto.response.LabResponse;
import com.example.exe101_bioverse.model.dto.response.ModelDetailResponse;
import com.example.exe101_bioverse.model.dto.response.ModelSummaryResponse;

import java.util.List;

/**
 * Service công khai cho học sinh / khách truy cập.
 */
public interface BioModelService {

    /**
     * Lấy danh sách model phổ biến (featured) cho trang chủ.
     */
    List<ModelSummaryResponse> getFeaturedModels();

    /**
     * Phân trang danh mục với bộ lọc.
     */
    PageResponse<ModelSummaryResponse> getCatalog(Integer grade, String category, String subject, String q, int page, int size);

    /**
     * Xem chi tiết model theo ID.
     */
    ModelDetailResponse getModelById(Long id);

    /**
     * Xem chi tiết model theo slug.
     */
    ModelDetailResponse getModelBySlug(String slug);

    /**
     * Lấy danh sách thể loại (category) riêng biệt, có thể lọc theo môn.
     */
    List<String> getCategories(String subject);

    /**
     * Lab đang mở cho catalog / nhân mẫu.
     */
    List<LabResponse> getLabs();

    /**
     * Lấy bài kiểm tra 3D theo Model ID.
     */
    com.example.exe101_bioverse.exam.dto.response.StudentExamPaperResponse getExamByModelId(Long id);

    /**
     * Lấy bài kiểm tra 3D theo Model Slug.
     */
    com.example.exe101_bioverse.exam.dto.response.StudentExamPaperResponse getExamByModelSlug(String slug);
}
