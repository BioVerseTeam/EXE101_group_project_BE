package com.example.exe101_bioverse.model.service;

import com.example.exe101_bioverse.model.dto.request.CreateLabRequest;
import com.example.exe101_bioverse.model.dto.request.UpdateLabRequest;
import com.example.exe101_bioverse.model.dto.response.LabResponse;
import com.example.exe101_bioverse.model.entity.BioLab;

import java.util.List;

public interface BioLabService {

    List<LabResponse> listPublic();

    List<LabResponse> listAdmin();

    LabResponse create(CreateLabRequest request);

    LabResponse update(Long id, UpdateLabRequest request);

    void delete(Long id);

    BioLab ensureCoded(String code, String fallbackName);
}
