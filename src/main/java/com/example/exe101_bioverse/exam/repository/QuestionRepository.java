package com.example.exe101_bioverse.exam.repository;

import com.example.exe101_bioverse.exam.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
