package com.example.exe101_bioverse.exam.repository;

import com.example.exe101_bioverse.exam.entity.Exam;
import com.example.exe101_bioverse.exam.enums.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long>, JpaSpecificationExecutor<Exam> {
    List<Exam> findByCode(String code);

    Optional<Exam> findFirstByCode(String code);

    Optional<Exam> findByModelId(Long modelId);

    List<Exam> findByName(String name);

    List<Exam> findBySubjectName(String subjectName);

    List<Exam> findByType(ExamType type);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);
}
