package com.example.exe101_bioverse.repository;

import com.example.exe101_bioverse.entity.Exam;
import com.example.exe101_bioverse.enums.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByCode(String code);

    List<Exam> findByName(String name);

    List<Exam> findBySubjectName(String subjectName);

    List<Exam> findByType(ExamType type);
}
