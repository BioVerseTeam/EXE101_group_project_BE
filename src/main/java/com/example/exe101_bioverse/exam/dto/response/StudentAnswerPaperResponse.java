package com.example.exe101_bioverse.exam.dto.response;

import com.example.exe101_bioverse.exam.enums.AnswerType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAnswerPaperResponse {

    private Long id;

    private AnswerType type;

    private String content;

    @Builder.Default
    private List<AnswerImageResponse> images = new ArrayList<>();
}
