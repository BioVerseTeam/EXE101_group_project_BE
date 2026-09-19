package com.example.exe101_bioverse.model.service.impl;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.model.dto.request.CreateModelRequest;
import com.example.exe101_bioverse.model.dto.request.ToggleFeaturedRequest;
import com.example.exe101_bioverse.model.dto.request.UpdateModelRequest;
import com.example.exe101_bioverse.model.dto.response.ModelDetailResponse;
import com.example.exe101_bioverse.model.entity.BioModel;
import com.example.exe101_bioverse.model.repository.BioModelRepository;
import com.example.exe101_bioverse.model.service.AdminBioModelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminBioModelServiceImpl implements AdminBioModelService {

    private final BioModelRepository bioModelRepository;

    public AdminBioModelServiceImpl(BioModelRepository bioModelRepository) {
        this.bioModelRepository = bioModelRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ModelDetailResponse> listModels(String q, Boolean isFeatured, Boolean isActive, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        String keyword = (q != null && !q.isBlank()) ? q.trim() : null;

        Page<BioModel> modelPage = bioModelRepository.findAdminModels(keyword, isFeatured, isActive, pageable);
        Page<ModelDetailResponse> responsePage = modelPage.map(ModelDetailResponse::from);
        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public ModelDetailResponse getModel(Long id) {
        BioModel model = bioModelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_NOT_FOUND));
        return ModelDetailResponse.from(model);
    }

    @Override
    public ModelDetailResponse createModel(CreateModelRequest request) {
        // Kiểm tra slug trùng
        if (request.getSlug() != null && bioModelRepository.existsBySlug(request.getSlug())) {
            throw new AppException(ErrorCode.INVALID_DATA, "Slug đã tồn tại: " + request.getSlug());
        }

        BioModel model = BioModel.builder()
                .name(request.getName())
                .nameEn(request.getNameEn())
                .slug(request.getSlug())
                .scientificName(request.getScientificName())
                .category(request.getCategory())
                .description(request.getDescription())
                .habitat(request.getHabitat())
                .characteristics(request.getCharacteristics())
                .classification(request.getClassification())
                .funFacts(request.getFunFacts())
                .grade(request.getGrade())
                .subject(request.getSubject() != null ? request.getSubject() : "BIOLOGY")
                .badgeText(request.getBadgeText())
                .actionText(request.getActionText() != null ? request.getActionText() : "Khám phá ngay")
                .actionIcon(request.getActionIcon() != null ? request.getActionIcon() : "3d_rotation")
                .targetMode(request.getTargetMode())
                .modelUrl(request.getModelUrl())
                .thumbnailUrl(request.getThumbnailUrl())
                .modelFormat(request.getModelFormat() != null ? request.getModelFormat() : "glb")
                .modelSizeBytes(request.getModelSizeBytes())
                .defaultScale(request.getDefaultScale())
                .defaultRotation(request.getDefaultRotation())
                .cameraPosition(request.getCameraPosition())
                .annotations(request.getAnnotations())
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .lessonId(request.getLessonId())
                .build();

        model = bioModelRepository.save(model);
        return ModelDetailResponse.from(model);
    }

    @Override
    public ModelDetailResponse updateModel(Long id, UpdateModelRequest request) {
        BioModel model = bioModelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_NOT_FOUND));

        // Kiểm tra slug trùng (trừ model hiện tại)
        if (request.getSlug() != null && bioModelRepository.existsBySlugAndIdNot(request.getSlug(), id)) {
            throw new AppException(ErrorCode.INVALID_DATA, "Slug đã tồn tại: " + request.getSlug());
        }

        // Chỉ cập nhật các field được gửi (non-null)
        if (request.getName() != null) model.setName(request.getName());
        if (request.getNameEn() != null) model.setNameEn(request.getNameEn());
        if (request.getSlug() != null) model.setSlug(request.getSlug());
        if (request.getScientificName() != null) model.setScientificName(request.getScientificName());
        if (request.getCategory() != null) model.setCategory(request.getCategory());
        if (request.getDescription() != null) model.setDescription(request.getDescription());
        if (request.getHabitat() != null) model.setHabitat(request.getHabitat());
        if (request.getCharacteristics() != null) model.setCharacteristics(request.getCharacteristics());
        if (request.getClassification() != null) model.setClassification(request.getClassification());
        if (request.getFunFacts() != null) model.setFunFacts(request.getFunFacts());
        if (request.getGrade() != null) model.setGrade(request.getGrade());
        if (request.getSubject() != null) model.setSubject(request.getSubject());
        if (request.getBadgeText() != null) model.setBadgeText(request.getBadgeText());
        if (request.getActionText() != null) model.setActionText(request.getActionText());
        if (request.getActionIcon() != null) model.setActionIcon(request.getActionIcon());
        if (request.getTargetMode() != null) model.setTargetMode(request.getTargetMode());
        if (request.getModelUrl() != null) model.setModelUrl(request.getModelUrl());
        if (request.getThumbnailUrl() != null) model.setThumbnailUrl(request.getThumbnailUrl());
        if (request.getModelFormat() != null) model.setModelFormat(request.getModelFormat());
        if (request.getModelSizeBytes() != null) model.setModelSizeBytes(request.getModelSizeBytes());
        if (request.getDefaultScale() != null) model.setDefaultScale(request.getDefaultScale());
        if (request.getDefaultRotation() != null) model.setDefaultRotation(request.getDefaultRotation());
        if (request.getCameraPosition() != null) model.setCameraPosition(request.getCameraPosition());
        if (request.getAnnotations() != null) model.setAnnotations(request.getAnnotations());
        if (request.getIsFeatured() != null) model.setIsFeatured(request.getIsFeatured());
        if (request.getIsActive() != null) model.setIsActive(request.getIsActive());
        if (request.getSortOrder() != null) model.setSortOrder(request.getSortOrder());
        if (request.getLessonId() != null) model.setLessonId(request.getLessonId());

        model = bioModelRepository.save(model);
        return ModelDetailResponse.from(model);
    }

    @Override
    public ModelDetailResponse toggleFeatured(Long id, ToggleFeaturedRequest request) {
        BioModel model = bioModelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_NOT_FOUND));

        model.setIsFeatured(request.getIsFeatured());
        if (request.getSortOrder() != null) {
            model.setSortOrder(request.getSortOrder());
        }

        model = bioModelRepository.save(model);
        return ModelDetailResponse.from(model);
    }

    @Override
    public void deleteModel(Long id) {
        BioModel model = bioModelRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_NOT_FOUND));

        // Soft delete
        model.setIsActive(false);
        model.setIsFeatured(false);
        bioModelRepository.save(model);
    }
}
