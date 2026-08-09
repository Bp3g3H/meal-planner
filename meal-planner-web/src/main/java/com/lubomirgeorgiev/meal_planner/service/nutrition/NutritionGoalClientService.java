package com.lubomirgeorgiev.meal_planner.service.nutrition;

import com.lubomirgeorgiev.meal_planner.model.dto.nutrition_goal.NutritionGoalRequest;
import com.lubomirgeorgiev.meal_planner.model.dto.nutrition_goal.NutritionGoalResponse;
import com.lubomirgeorgiev.meal_planner.service.nutrition.client.NutritionServiceClient;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NutritionGoalClientService {

    private final NutritionServiceClient nutritionServiceClient;

    public NutritionGoalClientService(NutritionServiceClient nutritionServiceClient) {
        this.nutritionServiceClient = nutritionServiceClient;
    }

    public NutritionGoalResponse getGoalOrNull(UUID userId) {
        try {
            return nutritionServiceClient.getGoalOrNull(userId);
        } catch (FeignException.NotFound e) {
            return null;
        }
    }

    public NutritionGoalResponse saveGoal(UUID userId, Integer target) {
        NutritionGoalResponse existing = getGoalOrNull(userId);
        NutritionGoalRequest request = NutritionGoalRequest.builder()
                .externalUserId(userId)
                .dailyCalorieTarget(target)
                .build();

        return existing == null
                ? nutritionServiceClient.createGoal(request)
                : nutritionServiceClient.updateGoal(request);
    }
}
