package com.example.exe101_bioverse.feedback.repository;

import com.example.exe101_bioverse.feedback.entity.Feedback;
import com.example.exe101_bioverse.feedback.enums.FeedbackCategory;
import com.example.exe101_bioverse.feedback.enums.FeedbackStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT f FROM Feedback f LEFT JOIN f.user u WHERE " +
           "(:status IS NULL OR f.status = :status) AND " +
           "(:category IS NULL OR f.category = :category) AND " +
           "(:rating IS NULL OR f.rating = :rating) AND " +
           "(:pattern IS NULL OR (" +
           "  LOWER(COALESCE(f.userName, '')) LIKE :pattern OR " +
           "  LOWER(COALESCE(f.userEmail, '')) LIKE :pattern OR " +
           "  LOWER(COALESCE(u.fullName, '')) LIKE :pattern OR " +
           "  LOWER(COALESCE(u.email, '')) LIKE :pattern OR " +
           "  LOWER(COALESCE(f.title, '')) LIKE :pattern OR " +
           "  LOWER(COALESCE(f.content, '')) LIKE :pattern" +
           ")) " +
           "ORDER BY f.createdAt DESC")
    Page<Feedback> search(
            @Param("status") FeedbackStatus status,
            @Param("category") FeedbackCategory category,
            @Param("rating") Integer rating,
            @Param("pattern") String pattern,
            Pageable pageable);

    @Query("SELECT f FROM Feedback f LEFT JOIN f.user u WHERE " +
           "(:status IS NULL OR f.status = :status) AND " +
           "(:category IS NULL OR f.category = :category) AND " +
           "(:rating IS NULL OR f.rating = :rating) AND " +
           "(:pattern IS NULL OR (" +
           "  LOWER(COALESCE(f.userName, '')) LIKE :pattern OR " +
           "  LOWER(COALESCE(f.userEmail, '')) LIKE :pattern OR " +
           "  LOWER(COALESCE(u.fullName, '')) LIKE :pattern OR " +
           "  LOWER(COALESCE(u.email, '')) LIKE :pattern OR " +
           "  LOWER(COALESCE(f.title, '')) LIKE :pattern OR " +
           "  LOWER(COALESCE(f.content, '')) LIKE :pattern" +
           ")) " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findAllMatching(
            @Param("status") FeedbackStatus status,
            @Param("category") FeedbackCategory category,
            @Param("rating") Integer rating,
            @Param("pattern") String pattern);

    long countByStatus(FeedbackStatus status);
}
