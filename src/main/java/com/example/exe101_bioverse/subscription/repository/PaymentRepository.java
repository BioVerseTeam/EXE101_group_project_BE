package com.example.exe101_bioverse.subscription.repository;

import com.example.exe101_bioverse.subscription.entity.Payment;
import com.example.exe101_bioverse.subscription.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderCode(Long orderCode);

    Page<Payment> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<Payment> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    Page<Payment> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Payment> findAllByStatusOrderByCreatedAtDesc(PaymentStatus status, Pageable pageable);

    @Query("SELECT p FROM Payment p JOIN p.user u WHERE " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(LOWER(u.email) LIKE :pattern OR LOWER(u.fullName) LIKE :pattern OR CAST(p.orderCode AS string) LIKE :pattern) " +
           "ORDER BY p.createdAt DESC")
    Page<Payment> searchWithKeyword(
            @Param("status") PaymentStatus status,
            @Param("pattern") String pattern,
            Pageable pageable);
}
