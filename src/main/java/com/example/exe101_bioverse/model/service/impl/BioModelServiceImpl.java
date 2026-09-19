package com.example.exe101_bioverse.model.service.impl;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.model.dto.response.ModelDetailResponse;
import com.example.exe101_bioverse.model.dto.response.ModelSummaryResponse;
import com.example.exe101_bioverse.model.dto.response.LabResponse;
import com.example.exe101_bioverse.model.entity.BioModel;
import com.example.exe101_bioverse.model.repository.BioModelRepository;
import com.example.exe101_bioverse.model.service.BioLabService;
import com.example.exe101_bioverse.model.service.BioModelCategoryService;
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
    private final BioModelCategoryService categoryService;
    private final BioLabService labService;

    public BioModelServiceImpl(
            BioModelRepository bioModelRepository,
            BioModelCategoryService categoryService,
            BioLabService labService
    ) {
        this.bioModelRepository = bioModelRepository;
        this.categoryService = categoryService;
        this.labService = labService;
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

        // Dùng chuỗi rỗng thay vì null — Hibernate bind String null thành bytea trên PostgreSQL
        String keyword = (q != null && !q.isBlank()) ? q.trim().toLowerCase() : "";
        String cat = (category != null && !category.isBlank()) ? category.trim() : "";
        String sub = (subject != null && !subject.isBlank()) ? subject.trim() : "";

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
    public List<String> getCategories(String subject) {
        String sub = (subject != null && !subject.isBlank()) ? subject.trim() : "";
        List<String> managed = categoryService.listPublic().stream()
                .filter(item -> sub.isEmpty() || sub.equalsIgnoreCase(item.getSubject()) || item.getSubject() == null)
                .map(item -> item.getName())
                .toList();
        if (!managed.isEmpty()) {
            return managed;
        }
        return bioModelRepository.findDistinctCategories(sub);
    }

    @Override
    public List<LabResponse> getLabs() {
        return labService.listPublic();
    }
}
