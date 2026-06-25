package com.example.exe101_bioverse.repository;

import com.example.exe101_bioverse.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
