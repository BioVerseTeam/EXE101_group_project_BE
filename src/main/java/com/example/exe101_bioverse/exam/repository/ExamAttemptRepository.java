package com.example.exe101_bioverse.exam.repository;

import com.example.exe101_bioverse.exam.entity.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {

    List<ExamAttempt> findByUserIdOrderBySubmittedAtDesc(Long userId);

    long countByUserId(Long userId);

    @Query("SELECT AVG(ea.score) FROM ExamAttempt ea WHERE ea.user.id = :userId")
    Double findAverageScoreByUserId(@Param("userId") Long userId);

    @Query("SELECT MAX(ea.score) FROM ExamAttempt ea WHERE ea.user.id = :userId")
    Double findMaxScoreByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(ea.timeSpentSec) FROM ExamAttempt ea WHERE ea.user.id = :userId")
    Long findTotalTimeSpentByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(ea.correctCount) FROM ExamAttempt ea WHERE ea.user.id = :userId")
    Integer findTotalCorrectCountByUserId(@Param("userId") Long userId);
}
