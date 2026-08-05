package com.lubomirgeorgiev.meal_planner.model.dto.nutrition_goal;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NutritionGoalResponse {

    private UUID id;
    private UUID externalUserId;
    private Integer dailyCalorieTarget;
    private LocalDateTime updatedOn;
}
