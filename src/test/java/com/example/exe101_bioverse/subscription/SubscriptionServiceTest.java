package com.example.exe101_bioverse.subscription;

import com.example.exe101_bioverse.auth.entity.User;
import com.example.exe101_bioverse.auth.repository.UserRepository;
import com.example.exe101_bioverse.subscription.config.PayosConfig;
import com.example.exe101_bioverse.subscription.dto.response.UserSubscriptionStatusResponse;
import com.example.exe101_bioverse.subscription.entity.Plan;
import com.example.exe101_bioverse.subscription.entity.Subscription;
import com.example.exe101_bioverse.subscription.enums.PlanDuration;
import com.example.exe101_bioverse.subscription.enums.PlanStatus;
import com.example.exe101_bioverse.subscription.enums.SubscriptionStatus;
import com.example.exe101_bioverse.subscription.repository.SubscriptionRepository;
import com.example.exe101_bioverse.subscription.service.impl.SubscriptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PayosConfig payosConfig;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    private User sampleUser;
    private Plan monthlyPlan;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .email("student@bioverse.vn")
                .fullName("Nguyễn Văn A")
                .build();

        monthlyPlan = Plan.builder()
                .id(10L)
                .name("Gói Tháng")
                .slug("monthly")
                .duration(PlanDuration.MONTHLY)
                .durationDays(30)
                .price(BigDecimal.valueOf(49000))
                .status(PlanStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("User status khi hệ thống bật Free Access Mode")
    void testUserStatus_FreeAccessMode() {
        when(payosConfig.isEnforceQuota()).thenReturn(false);

        UserSubscriptionStatusResponse response = subscriptionService.getUserStatus(1L, "STUDENT");

        assertNotNull(response);
        assertTrue(response.getIsFreeAccessMode());
        assertTrue(response.getIsPremium());
    }

    @Test
    @DisplayName("Kích hoạt gói lần đầu tiên cho người dùng")
    void testActivateSubscription_FirstTime() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(subscriptionRepository.findActiveSubscriptions(eq(1L), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(subscriptionRepository.save(any(Subscription.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Subscription activated = subscriptionService.activateSubscription(1L, monthlyPlan, null);

        assertNotNull(activated);
        assertEquals(SubscriptionStatus.ACTIVE, activated.getStatus());
        assertEquals(monthlyPlan, activated.getPlan());
        assertNotNull(activated.getStartDate());
        assertNotNull(activated.getEndDate());
        assertTrue(activated.getEndDate().isAfter(activated.getStartDate()));
    }

    @Test
    @DisplayName("Cộng dồn thời hạn khi người dùng đang có gói ACTIVE còn hạn")
    void testActivateSubscription_StackingRenewal() {
        LocalDateTime futureEnd = LocalDateTime.now().plusDays(15);
        Subscription existingActive = Subscription.builder()
                .id(99L)
                .user(sampleUser)
                .plan(monthlyPlan)
                .status(SubscriptionStatus.ACTIVE)
                .startDate(LocalDateTime.now().minusDays(15))
                .endDate(futureEnd)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(subscriptionRepository.findActiveSubscriptions(eq(1L), any(LocalDateTime.class)))
                .thenReturn(List.of(existingActive));
        when(subscriptionRepository.save(any(Subscription.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Subscription activated = subscriptionService.activateSubscription(1L, monthlyPlan, null);

        assertNotNull(activated);
        assertEquals(SubscriptionStatus.ACTIVE, activated.getStatus());
        // Ngày kết thúc mới phải bằng ngày kết thúc cũ + 30 ngày
        assertTrue(activated.getEndDate().isAfter(futureEnd));
    }
}
