package com.example.exe101_bioverse.model.repository;

import com.example.exe101_bioverse.model.entity.ReactionEquation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionEquationRepository extends JpaRepository<ReactionEquation, Long> {

    List<ReactionEquation> findByIsActiveTrueOrderBySortOrderAscTitleAsc();

    List<ReactionEquation> findAllByOrderBySortOrderAscTitleAsc();

    Optional<ReactionEquation> findByCodeIgnoreCase(String code);

    Optional<ReactionEquation> findByCodeIgnoreCaseAndIsActiveTrue(String code);

    boolean existsByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCaseAndIdNot(String code, Long id);
}
