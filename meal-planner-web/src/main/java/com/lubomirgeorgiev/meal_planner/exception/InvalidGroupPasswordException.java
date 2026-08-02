package com.lubomirgeorgiev.meal_planner.exception;

public class InvalidGroupPasswordException extends RuntimeException {
    public InvalidGroupPasswordException () {
        super("Invalid group password");
    }
}
