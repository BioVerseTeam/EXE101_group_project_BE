package com.example.exe101_bioverse.exam.repository;

import com.example.exe101_bioverse.exam.entity.Question;
import com.example.exe101_bioverse.exam.enums.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {
    List<Question> findByType(QuestionType type);

    @Query("SELECT q FROM Question q JOIN ExamQuestion eq ON q.id = eq.question.id WHERE eq.exam.id = :examId")
    List<Question> findByExamId(Long examId);
}
