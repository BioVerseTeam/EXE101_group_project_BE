package com.example.exe101_bioverse.repository;

import com.example.exe101_bioverse.entity.QuestionImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionImageRepository extends JpaRepository<QuestionImage, Long> {
    List<QuestionImage> findByQuestionId(Long questionId);
}
