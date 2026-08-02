package com.lubomirgeorgiev.meal_planner.exception;

public class GroupNotFoundException extends RuntimeException{
    public GroupNotFoundException () {
        super("Group not found");
    }
}
