package com.example.exe101_bioverse.subscription.dto.request;

import com.example.exe101_bioverse.subscription.enums.PlanDuration;
import com.example.exe101_bioverse.subscription.enums.PlanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlanRequest {

    private String name;

    private String slug;

    private String description;

    private PlanDuration duration;

    private Integer durationDays;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private List<String> features;

    private Integer maxDevices;

    private PlanStatus status;

    private Integer sortOrder;
}
