package com.lubomirgeorgiev.meal_planner.exception;

public class PasswordDoesNotMatchException extends RuntimeException {
    public PasswordDoesNotMatchException() {
        super("Passwords do not match");
    }
}
