package app.exception;

import java.util.UUID;

public class NutritionGoalAlreadyExistsException extends RuntimeException {
    public NutritionGoalAlreadyExistsException(UUID externalUserId) {
        super("Nutrition goal already exists for user: " + externalUserId);
    }
}
