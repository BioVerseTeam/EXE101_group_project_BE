package com.example.exe101_bioverse.exam.repository;

import com.example.exe101_bioverse.exam.entity.QuestionImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionImageRepository extends JpaRepository<QuestionImage, Long> {
    List<QuestionImage> findByQuestionId(Long questionId);
}
