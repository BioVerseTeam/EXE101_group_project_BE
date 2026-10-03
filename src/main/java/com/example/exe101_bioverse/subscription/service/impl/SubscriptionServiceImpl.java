package com.example.exe101_bioverse.subscription.service.impl;

import com.example.exe101_bioverse.auth.entity.User;
import com.example.exe101_bioverse.auth.repository.UserRepository;
import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.subscription.config.PayosConfig;
import com.example.exe101_bioverse.subscription.dto.response.SubscriptionResponse;
import com.example.exe101_bioverse.subscription.dto.response.UserSubscriptionStatusResponse;
import com.example.exe101_bioverse.subscription.entity.Plan;
import com.example.exe101_bioverse.subscription.entity.Subscription;
import com.example.exe101_bioverse.subscription.enums.SubscriptionStatus;
import com.example.exe101_bioverse.subscription.repository.SubscriptionRepository;
import com.example.exe101_bioverse.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final PayosConfig payosConfig;

    @Override
    @Transactional(readOnly = true)
    public UserSubscriptionStatusResponse getUserStatus(Long userId, String role) {
        boolean isFreeAccessMode = !payosConfig.isEnforceQuota();

        if (userId == null) {
            return UserSubscriptionStatusResponse.builder()
                    .isFreeAccessMode(isFreeAccessMode)
                    .isPremium(isFreeAccessMode)
                    .daysRemaining(null)
                    .expiresAt(null)
                    .activeSubscription(null)
                    .build();
        }

        // Nếu là ADMIN, luôn có full quyền Premium
        if ("ADMIN".equalsIgnoreCase(role)) {
            return UserSubscriptionStatusResponse.builder()
                    .isFreeAccessMode(isFreeAccessMode)
                    .isPremium(true)
                    .daysRemaining(9999L)
                    .expiresAt(null)
                    .activeSubscription(null)
                    .build();
        }

        LocalDateTime now = LocalDateTime.now();
        Optional<Subscription> activeSubOpt = subscriptionRepository
                .findTopByUserIdAndStatusAndEndDateAfterOrderByEndDateDesc(userId, SubscriptionStatus.ACTIVE, now);

        if (activeSubOpt.isPresent()) {
            Subscription sub = activeSubOpt.get();
            long daysRemaining = Math.max(0, Duration.between(now, sub.getEndDate()).toDays());
            return UserSubscriptionStatusResponse.builder()
                    .isFreeAccessMode(isFreeAccessMode)
                    .isPremium(true)
                    .daysRemaining(daysRemaining)
                    .expiresAt(sub.getEndDate())
                    .activeSubscription(SubscriptionResponse.from(sub))
                    .build();
        }

        return UserSubscriptionStatusResponse.builder()
                .isFreeAccessMode(isFreeAccessMode)
                .isPremium(isFreeAccessMode) // Nếu hệ thống đang mở free toàn bộ thì user được tính là có trải nghiệm Premium
                .daysRemaining(0L)
                .expiresAt(null)
                .activeSubscription(null)
                .build();
    }

    @Override
    public Subscription createPendingSubscription(Long userId, Plan plan) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Subscription sub = Subscription.builder()
                .user(user)
                .plan(plan)
                .status(SubscriptionStatus.PENDING)
                .autoRenew(false)
                .build();

        return subscriptionRepository.save(sub);
    }

    @Override
    public Subscription activateSubscription(Long userId, Plan plan, Long subscriptionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Subscription subscription = null;
        if (subscriptionId != null) {
            subscription = subscriptionRepository.findById(subscriptionId).orElse(null);
        }

        if (subscription == null) {
            subscription = Subscription.builder()
                    .user(user)
                    .plan(plan)
                    .build();
        }

        LocalDateTime now = LocalDateTime.now();

        // Kiểm tra xem có subscription ACTIVE nào còn hạn không để cộng dồn
        List<Subscription> existingActive = subscriptionRepository.findActiveSubscriptions(userId, now);
        LocalDateTime baseStartDate = now;
        LocalDateTime baseEndDate = now;

        if (!existingActive.isEmpty()) {
            // Lấy thời điểm kết thúc xa nhất hiện tại
            LocalDateTime maxCurrentEnd = existingActive.stream()
                    .map(Subscription::getEndDate)
                    .filter(d -> d != null && d.isAfter(now))
                    .max(LocalDateTime::compareTo)
                    .orElse(now);
            baseStartDate = now;
            baseEndDate = maxCurrentEnd;
        }

        LocalDateTime finalEndDate = baseEndDate.plusDays(plan.getDurationDays());

        subscription.setPlan(plan);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setStartDate(baseStartDate);
        subscription.setEndDate(finalEndDate);

        Subscription saved = subscriptionRepository.save(subscription);
        log.info("Subscription activated for user ID {}: Plan '{}', ends at {}", userId, plan.getName(), finalEndDate);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponse> listUserSubscriptions(Long userId) {
        return subscriptionRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(SubscriptionResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<SubscriptionResponse> searchSubscriptions(SubscriptionStatus status, String keyword, Pageable pageable) {
        Page<Subscription> page;
        String kw = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        if (kw != null) {
            String pattern = "%" + kw.toLowerCase() + "%";
            page = subscriptionRepository.searchWithKeyword(status, pattern, pageable);
        } else if (status != null) {
            page = subscriptionRepository.findAllByStatusOrderByCreatedAtDesc(status, pageable);
        } else {
            page = subscriptionRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        return PageResponse.<SubscriptionResponse>builder()
                .items(page.getContent().stream().map(SubscriptionResponse::from).toList())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .build();
    }

    @Override
    @Scheduled(cron = "0 0 * * * *") // Chạy mỗi giờ
    public void expireOutdatedSubscriptions() {
        LocalDateTime now = LocalDateTime.now();
        List<Subscription> expired = subscriptionRepository.findByStatusAndEndDateBefore(SubscriptionStatus.ACTIVE, now);
        for (Subscription sub : expired) {
            sub.setStatus(SubscriptionStatus.EXPIRED);
        }
        if (!expired.isEmpty()) {
            subscriptionRepository.saveAll(expired);
            log.info("Expired {} outdated subscriptions at {}", expired.size(), now);
        }
    }
}
