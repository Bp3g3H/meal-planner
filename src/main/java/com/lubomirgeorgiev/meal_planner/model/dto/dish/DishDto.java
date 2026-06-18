package com.lubomirgeorgiev.meal_planner.model.dto.dish;

import com.lubomirgeorgiev.meal_planner.model.dto.user.UserDto;
import com.lubomirgeorgiev.meal_planner.model.entity.dish.DishCategory;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DishDto {

    private String name;
    private String description;
    private Integer calories;
    private DishCategory category;
    private String imageUrl;
    private LocalDateTime createdOn;
    private UserDto createdBy;
}
