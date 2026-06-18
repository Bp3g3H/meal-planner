package com.lubomirgeorgiev.meal_planner.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException (String message) {
        super(message);
    }
}
