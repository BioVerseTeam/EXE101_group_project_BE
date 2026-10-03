package com.example.exe101_bioverse.exam.dto.response;

import com.example.exe101_bioverse.exam.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamBuilderResponse {

    private ExamBasicInfo exam;
    private ExamSummaryInfo summary;
    private List<BuilderQuestionItem> questions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExamBasicInfo {
        private Long id;
        private String code;
        private String title;
        private Integer duration;
        private Double maxScore;
        private String subjectName;
        private String status;
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExamSummaryInfo {
        private int totalQuestions;
        private double totalPoints;
        @com.fasterxml.jackson.annotation.JsonProperty("isValidTotalPoints")
        private boolean isValidTotalPoints;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuilderQuestionItem {
        private Long examQuestionId;
        private Long questionId;
        private int questionOrder;
        private double point;
        private String content;
        private QuestionType type;
        private String difficultyLevel;
        private String topic;
        private String explanation;
        private List<BuilderImageItem> images;
        private List<BuilderAnswerItem> answers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuilderImageItem {
        private Long id;
        private String imageUrl;
        private String caption;
        private int imageOrder;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuilderAnswerItem {
        private Long id;
        private String content;
        @com.fasterxml.jackson.annotation.JsonProperty("isCorrect")
        private boolean isCorrect;
        private int answerOrder;
        private String explanation;
        private List<BuilderImageItem> images;
    }
}
