package com.lubomirgeorgiev.meal_planner.model.dto.nutrition_goal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NutritionGoalRequest {

    @NotNull
    private UUID externalUserId;

    @NotNull
    @Min(500)
    @Max(10000)
    private Integer dailyCalorieTarget;
}
