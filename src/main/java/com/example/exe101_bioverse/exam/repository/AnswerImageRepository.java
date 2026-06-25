package com.example.exe101_bioverse.exam.repository;

import com.example.exe101_bioverse.exam.dto.response.AnswerImageResponse;
import com.example.exe101_bioverse.exam.entity.AnswerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerImageRepository extends JpaRepository<AnswerImage, Long> {
    List<AnswerImage> getAnswerImagesByAnswerId(Long AnswerId);
}
