package com.lubomirgeorgiev.meal_planner.model.dto.dish;

import com.lubomirgeorgiev.meal_planner.model.entity.dish.DishCategory;
import com.lubomirgeorgiev.meal_planner.model.entity.user.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class DishDto {

    private UUID id;
    private String name;
    private String description;
    private double calories;
    private DishCategory category;
    private String imageUrl;
    private User createdBy;
    private LocalDateTime createdOn;
}
