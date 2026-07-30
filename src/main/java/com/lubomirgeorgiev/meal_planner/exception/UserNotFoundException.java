package com.lubomirgeorgiev.meal_planner.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super("Dish not found with id: " + id);
    }
}
