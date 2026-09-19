package com.example.exe101_bioverse.streak.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserStreakTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 9, 19, 10, 0);

    @Test
    void firstVisitStartsAtOneAndSetsLongest() {
        UserStreak streak = UserStreak.newFor(1L, NOW);
        streak.recordVisit(LocalDate.of(2026, 9, 19), NOW);

        assertEquals(1, streak.getCurrentStreak());
        assertEquals(1, streak.getLongestStreak());
        assertEquals(LocalDate.of(2026, 9, 19), streak.getLastCheckInDate());
        assertTrue(streak.checkedInOn(LocalDate.of(2026, 9, 19)));
    }

    @Test
    void sameDayDoesNotIncreaseStreak() {
        UserStreak streak = UserStreak.newFor(1L, NOW);
        streak.recordVisit(LocalDate.of(2026, 9, 19), NOW);
        streak.recordVisit(LocalDate.of(2026, 9, 19), NOW.plusHours(3));

        assertEquals(1, streak.getCurrentStreak());
        assertEquals(1, streak.getLongestStreak());
    }

    @Test
    void consecutiveDayIncrementsAndTracksLongest() {
        UserStreak streak = UserStreak.newFor(1L, NOW);
        streak.recordVisit(LocalDate.of(2026, 9, 18), NOW);
        streak.recordVisit(LocalDate.of(2026, 9, 19), NOW);

        assertEquals(2, streak.getCurrentStreak());
        assertEquals(2, streak.getLongestStreak());
    }

    @Test
    void missedDayResetsCurrentButKeepsLongest() {
        UserStreak streak = UserStreak.newFor(1L, NOW);
        streak.recordVisit(LocalDate.of(2026, 9, 16), NOW);
        streak.recordVisit(LocalDate.of(2026, 9, 17), NOW);
        streak.recordVisit(LocalDate.of(2026, 9, 18), NOW);
        streak.recordVisit(LocalDate.of(2026, 9, 20), NOW);

        assertEquals(1, streak.getCurrentStreak());
        assertEquals(3, streak.getLongestStreak());
        assertEquals(LocalDate.of(2026, 9, 20), streak.getLastCheckInDate());
        assertEquals(0, streak.effectiveCurrent(LocalDate.of(2026, 9, 22)));
        assertFalse(streak.checkedInOn(LocalDate.of(2026, 9, 21)));
    }
}
