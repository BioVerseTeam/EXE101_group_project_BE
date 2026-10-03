package com.example.exe101_bioverse.subscription.service.impl;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.model.util.SlugUtil;
import com.example.exe101_bioverse.subscription.dto.request.CreatePlanRequest;
import com.example.exe101_bioverse.subscription.dto.request.UpdatePlanRequest;
import com.example.exe101_bioverse.subscription.dto.response.PlanResponse;
import com.example.exe101_bioverse.subscription.entity.Plan;
import com.example.exe101_bioverse.subscription.enums.PlanStatus;
import com.example.exe101_bioverse.subscription.repository.PlanRepository;
import com.example.exe101_bioverse.subscription.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PlanResponse> listPublicPlans() {
        return planRepository.findByStatusOrderBySortOrderAsc(PlanStatus.ACTIVE)
                .stream()
                .map(PlanResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanResponse> listAdminPlans() {
        return planRepository.findAllByOrderBySortOrderAsc()
                .stream()
                .map(PlanResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PlanResponse getPlanById(Long id) {
        return PlanResponse.from(getEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Plan getEntityById(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PLAN_NOT_FOUND));
    }

    @Override
    public PlanResponse createPlan(CreatePlanRequest request) {
        String name = request.getName().trim();
        String slug = (request.getSlug() != null && !request.getSlug().isBlank())
                ? SlugUtil.slugify(request.getSlug())
                : SlugUtil.slugify(name);

        if (planRepository.existsBySlug(slug)) {
            throw new AppException(ErrorCode.PLAN_SLUG_EXISTS);
        }

        Plan plan = Plan.builder()
                .name(name)
                .slug(slug)
                .description(request.getDescription())
                .duration(request.getDuration())
                .durationDays(request.getDurationDays())
                .price(request.getPrice())
                .originalPrice(request.getOriginalPrice())
                .features(request.getFeatures() != null ? request.getFeatures() : new ArrayList<>())
                .maxDevices(request.getMaxDevices() != null ? request.getMaxDevices() : 1)
                .status(request.getStatus() != null ? request.getStatus() : PlanStatus.ACTIVE)
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .build();

        return PlanResponse.from(planRepository.save(plan));
    }

    @Override
    public PlanResponse updatePlan(Long id, UpdatePlanRequest request) {
        Plan plan = getEntityById(id);

        if (request.getName() != null && !request.getName().isBlank()) {
            plan.setName(request.getName().trim());
        }

        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            String slug = SlugUtil.slugify(request.getSlug());
            if (planRepository.existsBySlugAndIdNot(slug, id)) {
                throw new AppException(ErrorCode.PLAN_SLUG_EXISTS);
            }
            plan.setSlug(slug);
        }

        if (request.getDescription() != null) {
            plan.setDescription(request.getDescription());
        }

        if (request.getDuration() != null) {
            plan.setDuration(request.getDuration());
        }

        if (request.getDurationDays() != null) {
            plan.setDurationDays(request.getDurationDays());
        }

        if (request.getPrice() != null) {
            plan.setPrice(request.getPrice());
        }

        if (request.getOriginalPrice() != null) {
            plan.setOriginalPrice(request.getOriginalPrice());
        }

        if (request.getFeatures() != null) {
            plan.setFeatures(request.getFeatures());
        }

        if (request.getMaxDevices() != null) {
            plan.setMaxDevices(request.getMaxDevices());
        }

        if (request.getStatus() != null) {
            plan.setStatus(request.getStatus());
        }

        if (request.getSortOrder() != null) {
            plan.setSortOrder(request.getSortOrder());
        }

        return PlanResponse.from(planRepository.save(plan));
    }

    @Override
    public void deletePlan(Long id) {
        Plan plan = getEntityById(id);
        // Lưu trữ (ARCHIVED) thay vì xóa cứng để bảo toàn dữ liệu lịch sử thanh toán
        plan.setStatus(PlanStatus.ARCHIVED);
        planRepository.save(plan);
    }
}
