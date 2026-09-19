package com.example.exe101_bioverse.streak.service.impl;

import com.example.exe101_bioverse.auth.dto.response.UserResponse;
import com.example.exe101_bioverse.auth.entity.User;
import com.example.exe101_bioverse.auth.enums.UserStatus;
import com.example.exe101_bioverse.auth.repository.UserRepository;
import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.streak.dto.response.StreakResponse;
import com.example.exe101_bioverse.streak.entity.UserStreak;
import com.example.exe101_bioverse.streak.repository.UserStreakRepository;
import com.example.exe101_bioverse.streak.service.StreakService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class StreakServiceImpl implements StreakService {

    private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private final UserStreakRepository userStreakRepository;
    private final UserRepository userRepository;

    public StreakServiceImpl(UserStreakRepository userStreakRepository, UserRepository userRepository) {
        this.userStreakRepository = userStreakRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public StreakResponse checkIn(Long userId) {
        requireActiveUser(userId);
        LocalDate today = LocalDate.now(VN_ZONE);
        LocalDateTime now = LocalDateTime.now(VN_ZONE);
        try {
            UserStreak streak = userStreakRepository.findByUserIdForUpdate(userId)
                    .orElseGet(() -> UserStreak.newFor(userId, now));
            streak.recordVisit(today, now);
            return toResponse(userStreakRepository.save(streak), today);
        } catch (DataIntegrityViolationException ex) {
            UserStreak streak = userStreakRepository.findByUserIdForUpdate(userId)
                    .orElseThrow(() -> ex);
            streak.recordVisit(today, now);
            return toResponse(userStreakRepository.save(streak), today);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public StreakResponse getStreak(Long userId) {
        requireActiveUser(userId);
        LocalDate today = LocalDate.now(VN_ZONE);
        return userStreakRepository.findById(userId)
                .map(streak -> StreakResponse.builder()
                        .currentStreak(streak.effectiveCurrent(today))
                        .longestStreak(streak.getLongestStreak() == null ? 0 : streak.getLongestStreak())
                        .lastCheckInDate(streak.getLastCheckInDate())
                        .checkedInToday(streak.checkedInOn(today))
                        .build())
                .orElseGet(() -> emptyStreak());
    }

    @Override
    public void applyTo(UserResponse user, StreakResponse streak) {
        if (user == null || streak == null) {
            return;
        }
        user.setCurrentStreak(streak.getCurrentStreak());
        user.setLongestStreak(streak.getLongestStreak());
        user.setLastCheckInDate(streak.getLastCheckInDate());
        user.setCheckedInToday(streak.getCheckedInToday());
    }

    private User requireActiveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
        }
        return user;
    }

    private StreakResponse toResponse(UserStreak streak, LocalDate today) {
        return StreakResponse.builder()
                .currentStreak(streak.getCurrentStreak() == null ? 0 : streak.getCurrentStreak())
                .longestStreak(streak.getLongestStreak() == null ? 0 : streak.getLongestStreak())
                .lastCheckInDate(streak.getLastCheckInDate())
                .checkedInToday(streak.checkedInOn(today))
                .build();
    }

    private StreakResponse emptyStreak() {
        return StreakResponse.builder()
                .currentStreak(0)
                .longestStreak(0)
                .lastCheckInDate(null)
                .checkedInToday(false)
                .build();
    }
}
