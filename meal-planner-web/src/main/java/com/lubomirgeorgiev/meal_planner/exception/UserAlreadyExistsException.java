package com.lubomirgeorgiev.meal_planner.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException () {
        super("User already exists");
    }
}
