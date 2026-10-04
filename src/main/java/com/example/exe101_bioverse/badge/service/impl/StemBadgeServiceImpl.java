package com.example.exe101_bioverse.badge.service.impl;

import com.example.exe101_bioverse.badge.dto.request.CreateBadgeRequest;
import com.example.exe101_bioverse.badge.dto.request.UpdateBadgeRequest;
import com.example.exe101_bioverse.badge.dto.response.BadgeResponse;
import com.example.exe101_bioverse.badge.entity.StemBadge;
import com.example.exe101_bioverse.badge.repository.StemBadgeRepository;
import com.example.exe101_bioverse.badge.service.StemBadgeService;
import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.model.util.SlugUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class StemBadgeServiceImpl implements StemBadgeService {

    private final StemBadgeRepository badgeRepository;

    public StemBadgeServiceImpl(StemBadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BadgeResponse> listPublic() {
        return badgeRepository.findByIsActiveTrueOrderBySortOrderAscNameAsc()
                .stream()
                .map(BadgeResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BadgeResponse> listAdmin() {
        return badgeRepository.findAllByOrderBySortOrderAscNameAsc()
                .stream()
                .map(BadgeResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BadgeResponse getById(Long id) {
        return BadgeResponse.from(require(id));
    }

    @Override
    public BadgeResponse create(CreateBadgeRequest request) {
        String name = request.getName().trim();
        String code = normalizeCode(request.getCode() != null && !request.getCode().isBlank()
                ? request.getCode()
                : name);

        if (badgeRepository.existsByCodeIgnoreCase(code)) {
            throw new AppException(ErrorCode.BADGE_CODE_EXISTS);
        }

        String filterTag = request.getFilterTag() != null ? request.getFilterTag().trim().toLowerCase(Locale.ROOT) : "starter";
        String defaultBg = resolveDefaultBg(filterTag);
        String defaultBorder = resolveDefaultBorder(filterTag);

        StemBadge badge = StemBadge.builder()
                .code(code)
                .name(name)
                .icon(request.getIcon() != null ? request.getIcon().trim() : "🎖️")
                .categoryName(request.getCategoryName() != null ? request.getCategoryName().trim() : "Danh hiệu STEM")
                .filterTag(filterTag)
                .description(blankToNull(request.getDescription()))
                .criteriaType(request.getCriteriaType() != null ? request.getCriteriaType().trim() : "ALWAYS_UNLOCKED")
                .criteriaValue(request.getCriteriaValue() != null ? Math.max(0, request.getCriteriaValue()) : 0)
                .rewardXp(request.getRewardXp() != null ? Math.max(0, request.getRewardXp()) : 50)
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .bgUnlocked(request.getBgUnlocked() != null && !request.getBgUnlocked().isBlank() ? request.getBgUnlocked().trim() : defaultBg)
                .borderUnlocked(request.getBorderUnlocked() != null && !request.getBorderUnlocked().isBlank() ? request.getBorderUnlocked().trim() : defaultBorder)
                .build();

        return BadgeResponse.from(badgeRepository.save(badge));
    }

    @Override
    public BadgeResponse update(Long id, UpdateBadgeRequest request) {
        StemBadge badge = require(id);

        if (request.getName() != null) {
            String name = request.getName().trim();
            if (name.isBlank()) {
                throw new AppException(ErrorCode.INVALID_DATA, "Tên danh hiệu không được để trống");
            }
            badge.setName(name);
        }

        if (request.getCode() != null && !request.getCode().isBlank()) {
            String code = normalizeCode(request.getCode());
            if (!code.equalsIgnoreCase(badge.getCode()) && badgeRepository.existsByCodeIgnoreCase(code)) {
                throw new AppException(ErrorCode.BADGE_CODE_EXISTS);
            }
            badge.setCode(code);
        }

        if (request.getIcon() != null && !request.getIcon().isBlank()) {
            badge.setIcon(request.getIcon().trim());
        }

        if (request.getCategoryName() != null && !request.getCategoryName().isBlank()) {
            badge.setCategoryName(request.getCategoryName().trim());
        }

        if (request.getFilterTag() != null && !request.getFilterTag().isBlank()) {
            badge.setFilterTag(request.getFilterTag().trim().toLowerCase(Locale.ROOT));
        }

        if (request.getDescription() != null) {
            badge.setDescription(blankToNull(request.getDescription()));
        }

        if (request.getCriteriaType() != null && !request.getCriteriaType().isBlank()) {
            badge.setCriteriaType(request.getCriteriaType().trim());
        }

        if (request.getCriteriaValue() != null) {
            badge.setCriteriaValue(Math.max(0, request.getCriteriaValue()));
        }

        if (request.getRewardXp() != null) {
            badge.setRewardXp(Math.max(0, request.getRewardXp()));
        }

        if (request.getSortOrder() != null) {
            badge.setSortOrder(request.getSortOrder());
        }

        if (request.getIsActive() != null) {
            badge.setIsActive(request.getIsActive());
        }

        if (request.getBgUnlocked() != null && !request.getBgUnlocked().isBlank()) {
            badge.setBgUnlocked(request.getBgUnlocked().trim());
        }

        if (request.getBorderUnlocked() != null && !request.getBorderUnlocked().isBlank()) {
            badge.setBorderUnlocked(request.getBorderUnlocked().trim());
        }

        return BadgeResponse.from(badgeRepository.save(badge));
    }

    @Override
    public void delete(Long id) {
        StemBadge badge = require(id);
        badge.setIsActive(false);
        badgeRepository.save(badge);
    }

    @Override
    public BadgeResponse toggleStatus(Long id) {
        StemBadge badge = require(id);
        badge.setIsActive(!Boolean.TRUE.equals(badge.getIsActive()));
        return BadgeResponse.from(badgeRepository.save(badge));
    }

    private StemBadge require(Long id) {
        return badgeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BADGE_NOT_FOUND));
    }

    private static String normalizeCode(String raw) {
        if (raw == null || raw.isBlank()) return "badge-" + System.currentTimeMillis();
        String slug = SlugUtil.slugify(raw).toLowerCase(Locale.ROOT);
        return slug.startsWith("badge-") ? slug : "badge-" + slug;
    }

    private static String blankToNull(String text) {
        if (text == null) return null;
        String trimmed = text.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static String resolveDefaultBg(String filterTag) {
        return switch (filterTag) {
            case "streak" -> "bg-[#ffedd5]";
            case "lab" -> "bg-[#f3e8ff]";
            case "xp" -> "bg-[#fee2e2]";
            default -> "bg-[#e8f5e9]";
        };
    }

    private static String resolveDefaultBorder(String filterTag) {
        return switch (filterTag) {
            case "streak" -> "border-[#ea580c]";
            case "lab" -> "border-[#7c3aed]";
            case "xp" -> "border-[#dc2626]";
            default -> "border-[#2e7d32]";
        };
    }
}
