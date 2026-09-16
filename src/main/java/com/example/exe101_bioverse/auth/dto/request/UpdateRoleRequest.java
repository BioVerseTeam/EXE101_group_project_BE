package com.example.exe101_bioverse.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateRoleRequest {

    @Size(max = 50, message = "Role code must not exceed 50 characters")
    @Pattern(
            regexp = "^$|^[A-Za-z][A-Za-z0-9_]*$",
            message = "Role code must start with a letter and contain only letters, numbers, and underscores"
    )
    private String code;

    @Size(max = 255, message = "Role name must not exceed 255 characters")
    private String name;

    private String description;
}
