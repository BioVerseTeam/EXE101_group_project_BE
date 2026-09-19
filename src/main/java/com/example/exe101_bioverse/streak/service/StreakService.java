package com.example.exe101_bioverse.streak.service;

import com.example.exe101_bioverse.auth.dto.response.UserResponse;
import com.example.exe101_bioverse.streak.dto.response.StreakResponse;

public interface StreakService {

    StreakResponse checkIn(Long userId);

    StreakResponse getStreak(Long userId);

    void applyTo(UserResponse user, StreakResponse streak);
}
