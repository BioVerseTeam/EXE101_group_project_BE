package com.example.exe101_bioverse.subscription.service;

import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.subscription.dto.response.SubscriptionResponse;
import com.example.exe101_bioverse.subscription.dto.response.UserSubscriptionStatusResponse;
import com.example.exe101_bioverse.subscription.entity.Plan;
import com.example.exe101_bioverse.subscription.entity.Subscription;
import com.example.exe101_bioverse.subscription.enums.SubscriptionStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubscriptionService {

    UserSubscriptionStatusResponse getUserStatus(Long userId, String role);

    Subscription createPendingSubscription(Long userId, Plan plan);

    Subscription activateSubscription(Long userId, Plan plan, Long subscriptionId);

    List<SubscriptionResponse> listUserSubscriptions(Long userId);

    PageResponse<SubscriptionResponse> searchSubscriptions(SubscriptionStatus status, String keyword, Pageable pageable);

    void expireOutdatedSubscriptions();
}
