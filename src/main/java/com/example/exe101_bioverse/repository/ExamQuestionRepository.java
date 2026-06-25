package com.example.exe101_bioverse.repository;

import com.example.exe101_bioverse.entity.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {

    List<ExamQuestion> findByExamId(Long examId);
}
