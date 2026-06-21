package com.lubomirgeorgiev.meal_planner.mapper.dish;

import com.lubomirgeorgiev.meal_planner.model.dto.dish.DishDto;
import com.lubomirgeorgiev.meal_planner.model.entity.dish.Dish;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DishMapper {

    public static DishDto toDishDto(Dish dish) {
        return DishDto.builder()
                .id(dish.getId())
                .name(dish.getName())
                .description(dish.getDescription())
                .calories(dish.getCalories())
                .category(dish.getCategory())
                .imageUrl(dish.getImageUrl())
                .build();
    }
}
