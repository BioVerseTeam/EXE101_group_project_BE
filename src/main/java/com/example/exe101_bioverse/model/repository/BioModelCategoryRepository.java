package com.example.exe101_bioverse.model.repository;

import com.example.exe101_bioverse.model.entity.BioModelCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BioModelCategoryRepository extends JpaRepository<BioModelCategory, Long> {

    List<BioModelCategory> findByIsActiveTrueOrderBySortOrderAscNameAsc();

    List<BioModelCategory> findAllByOrderBySortOrderAscNameAsc();

    Optional<BioModelCategory> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);
}
