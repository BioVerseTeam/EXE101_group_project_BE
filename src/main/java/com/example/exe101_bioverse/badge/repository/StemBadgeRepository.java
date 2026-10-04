package com.example.exe101_bioverse.badge.repository;

import com.example.exe101_bioverse.badge.entity.StemBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StemBadgeRepository extends JpaRepository<StemBadge, Long> {

    List<StemBadge> findByIsActiveTrueOrderBySortOrderAscNameAsc();

    List<StemBadge> findAllByOrderBySortOrderAscNameAsc();

    Optional<StemBadge> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);
}
