package com.lubomirgeorgiev.meal_planner.exception;

public class GroupIsAlreadyUpgradedException extends RuntimeException {
    public GroupIsAlreadyUpgradedException() {
        super("Group is already upgraded");
    }
}
