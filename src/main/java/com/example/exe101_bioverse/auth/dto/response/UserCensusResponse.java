package com.example.exe101_bioverse.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCensusResponse {
    private long total;
    private long students;
    private long locked;
    private long newThisWeek;
    private long newThisMonth;
    @Builder.Default
    private List<MonthBucket> months = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthBucket {
        private int year;
        private int month;
        private String label;
        private long count;
    }
}
