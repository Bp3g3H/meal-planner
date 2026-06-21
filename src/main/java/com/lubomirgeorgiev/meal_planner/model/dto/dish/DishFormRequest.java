package com.lubomirgeorgiev.meal_planner.model.dto.dish;

import com.lubomirgeorgiev.meal_planner.model.entity.dish.DishCategory;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DishFormRequest {

    @NotBlank
    @Size(max = 100)
    private String name;
    @Size(max = 1000)
    private String description;
    @NotNull
    @Min(0)
    @Max(5000)
    private Integer calories;
    @NotNull
    private DishCategory category;
    private String imageUrl;
}
