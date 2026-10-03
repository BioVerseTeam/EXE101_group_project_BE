package com.example.exe101_bioverse.subscription.dto.request;

import com.example.exe101_bioverse.subscription.enums.PlanDuration;
import com.example.exe101_bioverse.subscription.enums.PlanStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlanRequest {

    @NotBlank(message = "Tên gói cước không được để trống")
    private String name;

    private String slug;

    private String description;

    @NotNull(message = "Loại chu kỳ gói không được để trống")
    private PlanDuration duration;

    @NotNull(message = "Số ngày hiệu lực không được để trống")
    @Min(value = 1, message = "Số ngày hiệu lực phải ít nhất là 1 ngày")
    private Integer durationDays;

    @NotNull(message = "Giá gói không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá không được nhỏ hơn 0")
    private BigDecimal price;

    private BigDecimal originalPrice;

    private List<String> features;

    private Integer maxDevices;

    private PlanStatus status;

    private Integer sortOrder;
}
