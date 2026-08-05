package com.lubomirgeorgiev.meal_planner.model.dto.compliance_check;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class ComplianceCheckRequest {

    @NotNull
    private UUID externalUserId;

    @NotNull
    private LocalDate checkDate;

    @NotNull
    @Min(0)
    private Integer totalCaloriesConsumed;

    @NotNull
    @Min(0)
    private Integer targetedCalories;
}
