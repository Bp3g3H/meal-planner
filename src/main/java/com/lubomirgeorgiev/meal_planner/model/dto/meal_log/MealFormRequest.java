package com.lubomirgeorgiev.meal_planner.model.dto.meal_log;

import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealPortionSize;
import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class MealFormRequest {

    @NotNull
    private UUID dishId;
    @NotNull
    private MealType mealType;
    @NotNull
    private MealPortionSize portionSize;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate loggedInOn;
    @Size(max = 500)
    private String notes;
}
