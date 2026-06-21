package com.lubomirgeorgiev.meal_planner.mapper.meal_log;

import com.lubomirgeorgiev.meal_planner.model.dto.meal_log.MealFormRequest;
import com.lubomirgeorgiev.meal_planner.model.dto.meal_log.MealLogDto;
import com.lubomirgeorgiev.meal_planner.model.dto.meal_log.MealLogRepresentation;
import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealLog;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MealLogMapper {

    public static MealLogDto toMealLogDto(MealLog log) {
        MealLogDto dto = MealLogDto.builder().build();
        dto.setDishId(log.getDish().getId());
        dto.setMealType(log.getMealType());
        dto.setPortionSize(log.getPortionSize());
        dto.setLoggedInOn(log.getLoggedInOn());
        dto.setNotes(log.getNotes());
        return dto;
    }

    public static MealLogRepresentation toRepresentation(MealLog log) {
        return MealLogRepresentation.builder()
                .id(log.getId())
                .userId(log.getUser().getId())
                .username(log.getUser().getUsername())
                .dishId(log.getDish().getId())
                .dishName(log.getDish().getName())
                .dishCalories(log.getDish().getCalories())
                .mealType(log.getMealType())
                .portionSize(log.getPortionSize())
                .loggedInOn(log.getLoggedInOn().toLocalDate())
                .notes(log.getNotes())
                .build();
    }

    public static MealFormRequest toMealFormRequest(MealLog mealLog) {
        return MealFormRequest.builder()
                .dishId(mealLog.getDish().getId())
                .mealType(mealLog.getMealType())
                .portionSize(mealLog.getPortionSize())
                .notes(mealLog.getNotes())
                .loggedInOn(mealLog.getLoggedInOn().toLocalDate())
                .build();
    }
}
