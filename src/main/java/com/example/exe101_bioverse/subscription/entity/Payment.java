package com.example.exe101_bioverse.subscription.entity;

import com.example.exe101_bioverse.auth.entity.User;
import com.example.exe101_bioverse.subscription.enums.PaymentMethod;
import com.example.exe101_bioverse.subscription.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(name = "order_code", nullable = false, unique = true)
    private Long orderCode;

    @Column(name = "payos_payment_link", length = 500)
    private String payosPaymentLink;

    @Column(name = "payos_transaction_id", length = 255)
    private String payosTransactionId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    @Builder.Default
    private String currency = "VND";

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "payment_status")
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "payment_method")
    @Builder.Default
    private PaymentMethod method = PaymentMethod.PAYOS;

    @Column(length = 500)
    private String description;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "failed_reason", length = 500)
    private String failedReason;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "webhook_data", columnDefinition = "jsonb")
    private Map<String, Object> webhookData;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
