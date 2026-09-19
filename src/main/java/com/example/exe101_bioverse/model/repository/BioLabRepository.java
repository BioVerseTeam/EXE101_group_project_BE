package com.example.exe101_bioverse.model.repository;

import com.example.exe101_bioverse.model.entity.BioLab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BioLabRepository extends JpaRepository<BioLab, Long> {

    List<BioLab> findByIsActiveTrueOrderBySortOrderAscNameAsc();

    List<BioLab> findAllByOrderBySortOrderAscNameAsc();

    Optional<BioLab> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCaseAndIdNot(String code, Long id);
}
