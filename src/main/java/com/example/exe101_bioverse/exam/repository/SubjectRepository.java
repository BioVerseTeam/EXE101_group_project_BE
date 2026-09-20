package com.example.exe101_bioverse.exam.repository;

import com.example.exe101_bioverse.exam.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findBySemesterId(Long semesterId);

    List<Subject> findByCode(String code);

    List<Subject> findByNameContainingIgnoreCase(String name);
}
