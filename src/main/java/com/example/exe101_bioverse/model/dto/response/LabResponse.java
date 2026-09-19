package com.example.exe101_bioverse.model.dto.response;

import com.example.exe101_bioverse.model.entity.BioLab;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabResponse {

    private Long id;
    private String code;
    private String name;
    private String description;
    private Boolean isSystem;
    private Boolean isActive;
    private Integer sortOrder;

    public static LabResponse from(BioLab lab) {
        return LabResponse.builder()
                .id(lab.getId())
                .code(lab.getCode())
                .name(lab.getName())
                .description(lab.getDescription())
                .isSystem(lab.getIsSystem())
                .isActive(lab.getIsActive())
                .sortOrder(lab.getSortOrder())
                .build();
    }
}
