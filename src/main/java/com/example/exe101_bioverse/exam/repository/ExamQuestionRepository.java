package com.example.exe101_bioverse.exam.repository;

import com.example.exe101_bioverse.exam.entity.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {

    List<ExamQuestion> findByExamId(Long examId);

    List<ExamQuestion> findByExamIdOrderByQuestionOrderAsc(Long examId);

    List<ExamQuestion> findByQuestionIdAndExamId(Long questionId, Long examId);

    Optional<ExamQuestion> findFirstByExamIdAndQuestionId(Long examId, Long questionId);

    void deleteByExamIdAndQuestionId(Long examId, Long questionId);

    void deleteByExamId(Long examId);

    long countByExamId(Long examId);
}
