package com.lubomirgeorgiev.meal_planner.model.dto.meal_log;

import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealPortionSize;
import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder

public class MealLogRepresentation {
    private UUID id;
    private UUID userId;
    private String username;
    private UUID dishId;
    private String dishName;
    private Integer dishCalories;
    private MealType mealType;
    private MealPortionSize portionSize;
    private LocalDate loggedInOn;
    private String notes;
}
