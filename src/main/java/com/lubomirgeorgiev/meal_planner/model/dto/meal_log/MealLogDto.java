package com.lubomirgeorgiev.meal_planner.model.dto.meal_log;

import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealType;
import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.PortionSize;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MealLogDto {

    UUID dishId;
    private MealType mealType;
    private String notes;
    private PortionSize portionSize;
    private LocalDateTime loggedInOn;
}
