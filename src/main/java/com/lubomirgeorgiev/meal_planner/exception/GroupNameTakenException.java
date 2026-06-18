package com.lubomirgeorgiev.meal_planner.exception;

public class GroupNameTakenException extends RuntimeException {
    public GroupNameTakenException (String name) {
        super("Group name already in use: " + name);
    }
}
