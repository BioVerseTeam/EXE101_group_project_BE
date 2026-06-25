package com.example.exe101_bioverse.repository;

import com.example.exe101_bioverse.entity.AnswerImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerImageRepository extends JpaRepository<AnswerImage, Long> {
    List<AnswerImage> findByQuestionId(Long questionId);
}
