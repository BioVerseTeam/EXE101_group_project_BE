package com.example.exe101_bioverse.model.service.impl;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.model.dto.request.CreateLabRequest;
import com.example.exe101_bioverse.model.dto.request.UpdateLabRequest;
import com.example.exe101_bioverse.model.dto.response.LabResponse;
import com.example.exe101_bioverse.model.entity.BioLab;
import com.example.exe101_bioverse.model.repository.BioLabRepository;
import com.example.exe101_bioverse.model.service.BioLabService;
import com.example.exe101_bioverse.model.util.SlugUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class BioLabServiceImpl implements BioLabService {

    private final BioLabRepository labRepository;

    public BioLabServiceImpl(BioLabRepository labRepository) {
        this.labRepository = labRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabResponse> listPublic() {
        return labRepository.findByIsActiveTrueOrderBySortOrderAscNameAsc()
                .stream()
                .map(LabResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabResponse> listAdmin() {
        return labRepository.findAllByOrderBySortOrderAscNameAsc()
                .stream()
                .map(LabResponse::from)
                .toList();
    }

    @Override
    public LabResponse create(CreateLabRequest request) {
        String name = request.getName().trim();
        String code = normalizeCode(request.getCode() != null && !request.getCode().isBlank()
                ? request.getCode()
                : name);
        if (labRepository.existsByCodeIgnoreCase(code)) {
            throw new AppException(ErrorCode.LAB_CODE_EXISTS);
        }
        BioLab lab = BioLab.builder()
                .code(code)
                .name(name)
                .description(blankToNull(request.getDescription()))
                .isSystem(false)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .build();
        return LabResponse.from(labRepository.save(lab));
    }

    @Override
    public LabResponse update(Long id, UpdateLabRequest request) {
        BioLab lab = require(id);
        if (Boolean.TRUE.equals(lab.getIsSystem()) && request.getCode() != null
                && !normalizeCode(request.getCode()).equalsIgnoreCase(lab.getCode())) {
            throw new AppException(ErrorCode.SYSTEM_LAB_PROTECTED);
        }
        if (request.getName() != null) {
            String name = request.getName().trim();
            if (name.isBlank()) {
                throw new AppException(ErrorCode.INVALID_DATA, "Tên lab không được để trống");
            }
            lab.setName(name);
        }
        if (request.getCode() != null && !Boolean.TRUE.equals(lab.getIsSystem())) {
            String code = normalizeCode(request.getCode());
            if (labRepository.existsByCodeIgnoreCaseAndIdNot(code, id)) {
                throw new AppException(ErrorCode.LAB_CODE_EXISTS);
            }
            lab.setCode(code);
        }
        if (request.getDescription() != null) {
            lab.setDescription(blankToNull(request.getDescription()));
        }
        if (request.getSortOrder() != null) {
            lab.setSortOrder(request.getSortOrder());
        }
        if (request.getIsActive() != null) {
            lab.setIsActive(request.getIsActive());
        }
        return LabResponse.from(labRepository.save(lab));
    }

    @Override
    public void delete(Long id) {
        BioLab lab = require(id);
        if (Boolean.TRUE.equals(lab.getIsSystem())) {
            throw new AppException(ErrorCode.SYSTEM_LAB_PROTECTED);
        }
        lab.setIsActive(false);
        labRepository.save(lab);
    }

    @Override
    public BioLab ensureCoded(String code, String fallbackName) {
        if (code == null || code.isBlank()) {
            return null;
        }
        String normalized = normalizeCode(code);
        return labRepository.findByCodeIgnoreCase(normalized).orElseGet(() -> {
            String name = fallbackName != null && !fallbackName.isBlank()
                    ? fallbackName.trim()
                    : humanize(normalized);
            return labRepository.save(BioLab.builder()
                    .code(normalized)
                    .name(name)
                    .isSystem(false)
                    .isActive(true)
                    .sortOrder(0)
                    .build());
        });
    }

    private BioLab require(Long id) {
        return labRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LAB_NOT_FOUND));
    }

    private String normalizeCode(String raw) {
        return SlugUtil.slugify(raw).toLowerCase(Locale.ROOT);
    }

    private String humanize(String code) {
        if (code == null || code.isBlank()) {
            return "Lab mới";
        }
        String spaced = code.replace('-', ' ').trim();
        if (spaced.isEmpty()) {
            return "Lab mới";
        }
        return Character.toUpperCase(spaced.charAt(0)) + spaced.substring(1);
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
