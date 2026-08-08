package com.lubomirgeorgiev.meal_planner.model.entity.meal_log;

public enum MealPortionSize {
    LARGE(1.5),
    MEDIUM(1.0),
    SMALL(0.75);

    private final double multiplier;

    MealPortionSize(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
