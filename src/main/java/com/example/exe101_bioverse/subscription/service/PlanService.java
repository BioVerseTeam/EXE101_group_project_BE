package com.example.exe101_bioverse.subscription.service;

import com.example.exe101_bioverse.subscription.dto.request.CreatePlanRequest;
import com.example.exe101_bioverse.subscription.dto.request.UpdatePlanRequest;
import com.example.exe101_bioverse.subscription.dto.response.PlanResponse;
import com.example.exe101_bioverse.subscription.entity.Plan;

import java.util.List;

public interface PlanService {

    List<PlanResponse> listPublicPlans();

    List<PlanResponse> listAdminPlans();

    PlanResponse getPlanById(Long id);

    Plan getEntityById(Long id);

    PlanResponse createPlan(CreatePlanRequest request);

    PlanResponse updatePlan(Long id, UpdatePlanRequest request);

    void deletePlan(Long id);
}
