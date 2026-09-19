package com.example.exe101_bioverse.exam.repository;

import com.example.exe101_bioverse.exam.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    Optional<Grade> findByGrade(Integer grade);

    List<Grade> findAllByOrderByGradeAsc();
}
