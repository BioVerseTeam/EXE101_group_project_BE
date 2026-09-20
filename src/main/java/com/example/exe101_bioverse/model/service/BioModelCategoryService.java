package com.example.exe101_bioverse.model.service;

import com.example.exe101_bioverse.model.dto.request.CreateCategoryRequest;
import com.example.exe101_bioverse.model.dto.request.UpdateCategoryRequest;
import com.example.exe101_bioverse.model.dto.response.CategoryResponse;
import com.example.exe101_bioverse.model.entity.BioModelCategory;

import java.util.List;

public interface BioModelCategoryService {

    List<CategoryResponse> listPublic();

    List<CategoryResponse> listAdmin();

    CategoryResponse create(CreateCategoryRequest request);

    CategoryResponse update(Long id, UpdateCategoryRequest request);

    void delete(Long id);

    BioModelCategory ensureNamed(String name);
}
