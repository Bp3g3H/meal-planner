package com.lubomirgeorgiev.meal_planner.model.dto.nutrition_goal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NutritionGoalFormDto {

    @NotNull
    @Min(500)
    @Max(10000)
    private Integer dailyCalorieTarget;
}
