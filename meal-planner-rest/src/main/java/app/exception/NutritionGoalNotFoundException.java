package app.exception;

import java.util.UUID;

public class NutritionGoalNotFoundException extends RuntimeException {
    public NutritionGoalNotFoundException(UUID externalUserId) {
        super("No nutrition goal found for user: " + externalUserId);
    }
}
