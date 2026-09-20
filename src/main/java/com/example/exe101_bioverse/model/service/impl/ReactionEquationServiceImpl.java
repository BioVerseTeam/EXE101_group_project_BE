package com.example.exe101_bioverse.model.service.impl;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.model.dto.request.CreateReactionRequest;
import com.example.exe101_bioverse.model.dto.request.UpdateReactionRequest;
import com.example.exe101_bioverse.model.dto.response.ReactionResponse;
import com.example.exe101_bioverse.model.entity.ReactionEquation;
import com.example.exe101_bioverse.model.repository.ReactionEquationRepository;
import com.example.exe101_bioverse.model.service.ReactionEquationService;
import com.example.exe101_bioverse.model.util.SlugUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class ReactionEquationServiceImpl implements ReactionEquationService {

    private final ReactionEquationRepository repository;
    private final JsonMapper jsonMapper;

    public ReactionEquationServiceImpl(ReactionEquationRepository repository, JsonMapper jsonMapper) {
        this.repository = repository;
        this.jsonMapper = jsonMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReactionResponse> listPublic() {
        return repository.findByIsActiveTrueOrderBySortOrderAscTitleAsc()
                .stream()
                .map(ReactionResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReactionResponse> listAdmin() {
        return repository.findAllByOrderBySortOrderAscTitleAsc()
                .stream()
                .map(ReactionResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReactionResponse getPublicByCode(String code) {
        ReactionEquation entity = repository.findByCodeIgnoreCaseAndIsActiveTrue(normalizeCode(code))
                .orElseThrow(() -> new AppException(ErrorCode.REACTION_NOT_FOUND));
        return ReactionResponse.from(entity);
    }

    @Override
    public ReactionResponse create(CreateReactionRequest request) {
        String title = request.getTitle().trim();
        String name = blankToFallback(request.getName(), title);
        String code = normalizeCode(request.getCode() != null && !request.getCode().isBlank()
                ? request.getCode()
                : title);
        if (repository.existsByCodeIgnoreCase(code)) {
            throw new AppException(ErrorCode.REACTION_CODE_EXISTS);
        }
        String chemxJson = normalizeChemx(request.getChemx());
        ReactionEquation entity = ReactionEquation.builder()
                .code(code)
                .title(title)
                .subtitle(blankToNull(request.getSubtitle()))
                .gradeLabel(blankToNull(request.getGradeLabel()))
                .name(name)
                .description(blankToNull(request.getDescription()))
                .chemxJson(chemxJson)
                .isSystem(false)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .build();
        return ReactionResponse.from(repository.save(entity));
    }

    @Override
    public ReactionResponse update(Long id, UpdateReactionRequest request) {
        ReactionEquation entity = require(id);
        if (Boolean.TRUE.equals(entity.getIsSystem()) && request.getCode() != null
                && !normalizeCode(request.getCode()).equalsIgnoreCase(entity.getCode())) {
            throw new AppException(ErrorCode.SYSTEM_REACTION_PROTECTED);
        }
        if (request.getTitle() != null) {
            String title = request.getTitle().trim();
            if (title.isBlank()) {
                throw new AppException(ErrorCode.INVALID_DATA, "Tiêu đề không được để trống");
            }
            entity.setTitle(title);
        }
        if (request.getName() != null) {
            String name = request.getName().trim();
            if (name.isBlank()) {
                throw new AppException(ErrorCode.INVALID_DATA, "Tên phương trình không được để trống");
            }
            entity.setName(name);
        }
        if (request.getSubtitle() != null) {
            entity.setSubtitle(blankToNull(request.getSubtitle()));
        }
        if (request.getGradeLabel() != null) {
            entity.setGradeLabel(blankToNull(request.getGradeLabel()));
        }
        if (request.getDescription() != null) {
            entity.setDescription(blankToNull(request.getDescription()));
        }
        if (request.getCode() != null && !Boolean.TRUE.equals(entity.getIsSystem())) {
            String code = normalizeCode(request.getCode());
            if (repository.existsByCodeIgnoreCaseAndIdNot(code, id)) {
                throw new AppException(ErrorCode.REACTION_CODE_EXISTS);
            }
            entity.setCode(code);
        }
        if (request.getChemx() != null) {
            entity.setChemxJson(normalizeChemx(request.getChemx()));
        }
        if (request.getSortOrder() != null) {
            entity.setSortOrder(request.getSortOrder());
        }
        if (request.getIsActive() != null) {
            entity.setIsActive(request.getIsActive());
        }
        return ReactionResponse.from(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        ReactionEquation entity = require(id);
        if (Boolean.TRUE.equals(entity.getIsSystem())) {
            throw new AppException(ErrorCode.SYSTEM_REACTION_PROTECTED);
        }
        entity.setIsActive(false);
        repository.save(entity);
    }

    private ReactionEquation require(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REACTION_NOT_FOUND));
    }

    private String normalizeChemx(Object chemx) {
        if (chemx == null) {
            throw new AppException(ErrorCode.INVALID_DATA, "Thiếu dữ liệu hoạt ảnh .chemx");
        }
        try {
            JsonNode node;
            if (chemx instanceof String raw) {
                String trimmed = raw.trim();
                if (trimmed.isBlank()) {
                    throw new AppException(ErrorCode.INVALID_DATA, "File .chemx trống");
                }
                node = jsonMapper.readTree(trimmed);
            } else {
                node = jsonMapper.valueToTree(chemx);
            }
            if (node == null || !node.path("keyframes").isArray() || node.path("keyframes").isEmpty()) {
                throw new AppException(ErrorCode.INVALID_DATA, "File .chemx thiếu danh sách keyframe");
            }
            return jsonMapper.writeValueAsString(node);
        } catch (AppException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AppException(ErrorCode.INVALID_DATA, "Nội dung .chemx không phải JSON hợp lệ");
        }
    }

    private String normalizeCode(String raw) {
        return SlugUtil.slugify(raw).toLowerCase(Locale.ROOT);
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private String blankToFallback(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.trim();
    }
}
