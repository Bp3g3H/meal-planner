package com.lubomirgeorgiev.meal_planner.model.dto.compliance_check;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class ComplianceCheckResponse {

    private UUID id;
    private LocalDate checkDate;
    private Integer totalCaloriesConsumed;
    private Integer targetCalories;
    private boolean withinTarget;
}
