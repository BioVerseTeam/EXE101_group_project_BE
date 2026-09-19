package com.example.exe101_bioverse.exam.repository;

import com.example.exe101_bioverse.exam.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {

    List<Semester> findByClassEntityId(Long classId);

    List<Semester> findByClassEntityIdOrderBySemesterOrderAsc(Long classId);
}
