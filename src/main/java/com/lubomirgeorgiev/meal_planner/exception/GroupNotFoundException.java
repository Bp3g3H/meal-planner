package com.lubomirgeorgiev.meal_planner.exception;

public class GroupNotFoundException extends RuntimeException{
    public GroupNotFoundException (String name) {
        super("No group found with name: " + name);
    }
}
