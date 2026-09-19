package com.example.exe101_bioverse.exam.repository;

import com.example.exe101_bioverse.exam.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {

    @Query("SELECT s FROM Semester s WHERE s.grade.id = :gradeId")
    List<Semester> findByGradeId(@Param("gradeId") Long gradeId);

    @Query("SELECT s FROM Semester s WHERE s.grade.id = :gradeId ORDER BY s.semesterOrder ASC")
    List<Semester> findByGradeIdOrderBySemesterOrderAsc(@Param("gradeId") Long gradeId);
}
