package com.example.exe101_bioverse.model.service.impl;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.model.dto.request.CreateCategoryRequest;
import com.example.exe101_bioverse.model.dto.request.UpdateCategoryRequest;
import com.example.exe101_bioverse.model.dto.response.CategoryResponse;
import com.example.exe101_bioverse.model.entity.BioModelCategory;
import com.example.exe101_bioverse.model.repository.BioModelCategoryRepository;
import com.example.exe101_bioverse.model.service.BioModelCategoryService;
import com.example.exe101_bioverse.model.util.SlugUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BioModelCategoryServiceImpl implements BioModelCategoryService {

    private final BioModelCategoryRepository categoryRepository;

    public BioModelCategoryServiceImpl(BioModelCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> listPublic() {
        return categoryRepository.findByIsActiveTrueOrderBySortOrderAscNameAsc()
                .stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> listAdmin() {
        return categoryRepository.findAllByOrderBySortOrderAscNameAsc()
                .stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @Override
    public CategoryResponse create(CreateCategoryRequest request) {
        String name = request.getName().trim();
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new AppException(ErrorCode.CATEGORY_NAME_EXISTS);
        }
        BioModelCategory category = BioModelCategory.builder()
                .name(name)
                .slug(uniqueSlug(SlugUtil.slugify(name), null))
                .subject(blankToNull(request.getSubject()))
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse update(Long id, UpdateCategoryRequest request) {
        BioModelCategory category = require(id);
        if (request.getName() != null) {
            String name = request.getName().trim();
            if (name.isBlank()) {
                throw new AppException(ErrorCode.INVALID_DATA, "Tên loại mẫu không được để trống");
            }
            if (categoryRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
                throw new AppException(ErrorCode.CATEGORY_NAME_EXISTS);
            }
            category.setName(name);
            category.setSlug(uniqueSlug(SlugUtil.slugify(name), id));
        }
        if (request.getSubject() != null) {
            category.setSubject(blankToNull(request.getSubject()));
        }
        if (request.getSortOrder() != null) {
            category.setSortOrder(request.getSortOrder());
        }
        if (request.getIsActive() != null) {
            category.setIsActive(request.getIsActive());
        }
        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        BioModelCategory category = require(id);
        category.setIsActive(false);
        categoryRepository.save(category);
    }

    @Override
    public BioModelCategory ensureNamed(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        String trimmed = name.trim();
        return categoryRepository.findByNameIgnoreCase(trimmed).orElseGet(() ->
                categoryRepository.save(BioModelCategory.builder()
                        .name(trimmed)
                        .slug(uniqueSlug(SlugUtil.slugify(trimmed), null))
                        .isActive(true)
                        .sortOrder(0)
                        .build()));
    }

    private BioModelCategory require(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private String uniqueSlug(String base, Long excludeId) {
        String slug = base;
        int i = 2;
        while (excludeId == null
                ? categoryRepository.existsBySlug(slug)
                : categoryRepository.existsBySlugAndIdNot(slug, excludeId)) {
            slug = base + "-" + i;
            i += 1;
        }
        return slug;
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
