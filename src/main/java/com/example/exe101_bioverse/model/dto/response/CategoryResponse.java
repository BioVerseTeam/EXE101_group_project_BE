package com.example.exe101_bioverse.model.dto.response;

import com.example.exe101_bioverse.model.entity.BioModelCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private Long id;
    private String name;
    private String slug;
    private String subject;
    private Integer sortOrder;
    private Boolean isActive;

    public static CategoryResponse from(BioModelCategory category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .subject(category.getSubject())
                .sortOrder(category.getSortOrder())
                .isActive(category.getIsActive())
                .build();
    }
}
