package com.example.exe101_bioverse.ai.dto;

import java.util.List;

public record PagedResponse<T>(
        List<T> items,
        String nextCursor,
        boolean hasMore
) {
}
