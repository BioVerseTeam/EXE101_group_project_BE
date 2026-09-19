package com.example.exe101_bioverse.model.service.impl;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.model.dto.response.ModelDetailResponse;
import com.example.exe101_bioverse.model.dto.response.ModelSummaryResponse;
import com.example.exe101_bioverse.model.entity.BioModel;
import com.example.exe101_bioverse.model.repository.BioModelRepository;
import com.example.exe101_bioverse.model.service.BioModelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BioModelServiceImpl implements BioModelService {

    private final BioModelRepository bioModelRepository;

    public BioModelServiceImpl(BioModelRepository bioModelRepository) {
        this.bioModelRepository = bioModelRepository;
    }

    @Override
    public List<ModelSummaryResponse> getFeaturedModels() {
        return bioModelRepository.findFeaturedModels()
                .stream()
                .map(ModelSummaryResponse::from)
                .toList();
    }

    @Override
    public PageResponse<ModelSummaryResponse> getCatalog(
            Integer grade, String category, String subject, String q, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "sortOrder"));

        // Normalize empty strings to null for flexible filtering
        String keyword = (q != null && !q.isBlank()) ? q.trim() : null;
        String cat = (category != null && !category.isBlank()) ? category.trim() : null;
        String sub = (subject != null && !subject.isBlank()) ? subject.trim() : null;

        Page<BioModel> modelPage = bioModelRepository.findCatalogModels(grade, cat, sub, keyword, pageable);

        Page<ModelSummaryResponse> responsePage = modelPage.map(ModelSummaryResponse::from);
        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional
    public ModelDetailResponse getModelById(Long id) {
        BioModel model = bioModelRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_NOT_FOUND));

        // Tăng lượt xem
        bioModelRepository.incrementViewCount(id);

        return ModelDetailResponse.from(model);
    }

    @Override
    @Transactional
    public ModelDetailResponse getModelBySlug(String slug) {
        BioModel model = bioModelRepository.findBySlugAndIsActiveTrue(slug)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_NOT_FOUND));

        // Tăng lượt xem
        bioModelRepository.incrementViewCount(model.getId());

        return ModelDetailResponse.from(model);
    }

    @Override
    public List<String> getCategories() {
        return bioModelRepository.findDistinctCategories();
    }
}
