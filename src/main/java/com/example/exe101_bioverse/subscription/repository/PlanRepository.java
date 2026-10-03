package com.example.exe101_bioverse.subscription.repository;

import com.example.exe101_bioverse.subscription.entity.Plan;
import com.example.exe101_bioverse.subscription.enums.PlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findBySlug(String slug);

    List<Plan> findByStatusOrderBySortOrderAsc(PlanStatus status);

    List<Plan> findAllByOrderBySortOrderAsc();

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);
}
