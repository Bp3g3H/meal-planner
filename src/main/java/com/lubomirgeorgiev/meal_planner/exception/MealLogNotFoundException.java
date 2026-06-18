package com.lubomirgeorgiev.meal_planner.exception;

import java.util.UUID;

public class MealLogNotFoundException  extends RuntimeException{

    public MealLogNotFoundException (UUID id) {
        super("Meal log not found with id: " + id);
    }
}
