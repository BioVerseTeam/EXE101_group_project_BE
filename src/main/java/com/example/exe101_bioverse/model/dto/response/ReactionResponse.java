package com.example.exe101_bioverse.model.dto.response;

import com.example.exe101_bioverse.model.entity.ReactionEquation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactionResponse {

    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    private Long id;
    private String code;
    private String title;
    private String subtitle;
    private String gradeLabel;
    private String name;
    private String description;
    private Object chemx;
    private Boolean isSystem;
    private Boolean isActive;
    private Integer sortOrder;
    private Integer keyframeCount;

    public static ReactionResponse from(ReactionEquation entity) {
        JsonNode chemxNode = parseChemx(entity.getChemxJson());
        int keyframes = 0;
        if (chemxNode != null && chemxNode.path("keyframes").isArray()) {
            keyframes = chemxNode.path("keyframes").size();
        }
        return ReactionResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .title(entity.getTitle())
                .subtitle(entity.getSubtitle())
                .gradeLabel(entity.getGradeLabel())
                .name(entity.getName())
                .description(entity.getDescription())
                .chemx(chemxNode)
                .isSystem(entity.getIsSystem())
                .isActive(entity.getIsActive())
                .sortOrder(entity.getSortOrder())
                .keyframeCount(keyframes)
                .build();
    }

    private static JsonNode parseChemx(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return MAPPER.readTree(raw);
        } catch (Exception ignored) {
            return MAPPER.getNodeFactory().textNode(raw);
        }
    }
}
