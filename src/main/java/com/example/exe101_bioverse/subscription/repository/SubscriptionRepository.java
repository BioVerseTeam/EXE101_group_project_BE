package com.example.exe101_bioverse.subscription.repository;

import com.example.exe101_bioverse.subscription.entity.Subscription;
import com.example.exe101_bioverse.subscription.enums.SubscriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT s FROM Subscription s WHERE s.user.id = :userId AND s.status = 'ACTIVE' AND s.endDate > :now ORDER BY s.endDate DESC")
    List<Subscription> findActiveSubscriptions(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    Optional<Subscription> findTopByUserIdAndStatusAndEndDateAfterOrderByEndDateDesc(
            Long userId, SubscriptionStatus status, LocalDateTime now);

    List<Subscription> findByStatusAndEndDateBefore(SubscriptionStatus status, LocalDateTime now);

    Page<Subscription> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Subscription> findAllByStatusOrderByCreatedAtDesc(SubscriptionStatus status, Pageable pageable);

    @Query("SELECT s FROM Subscription s JOIN s.user u WHERE " +
           "(:status IS NULL OR s.status = :status) AND " +
           "(LOWER(u.email) LIKE :pattern OR LOWER(u.fullName) LIKE :pattern) " +
           "ORDER BY s.createdAt DESC")
    Page<Subscription> searchWithKeyword(
            @Param("status") SubscriptionStatus status,
            @Param("pattern") String pattern,
            Pageable pageable);
}
