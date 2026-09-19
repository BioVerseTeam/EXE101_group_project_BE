package com.example.exe101_bioverse.exam.repository;

import com.example.exe101_bioverse.exam.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {

    Optional<ClassEntity> findByGrade(Integer grade);

    List<ClassEntity> findAllByOrderByGradeAsc();

    boolean existsByGrade(Integer grade);
}
