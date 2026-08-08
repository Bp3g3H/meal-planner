package com.lubomirgeorgiev.meal_planner.scheduling;

import com.lubomirgeorgiev.meal_planner.model.dto.compliance_check.ComplianceCheckRequest;
import com.lubomirgeorgiev.meal_planner.model.dto.nutrition_goal.NutritionGoalResponse;
import com.lubomirgeorgiev.meal_planner.model.dto.user.UserDto;
import com.lubomirgeorgiev.meal_planner.service.meal_log.MealLogService;
import com.lubomirgeorgiev.meal_planner.service.nutrition.NutritionGoalClientService;
import com.lubomirgeorgiev.meal_planner.service.nutrition.client.NutritionServiceClient;
import com.lubomirgeorgiev.meal_planner.service.user.UserService;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;

public class ComplianceCheckJob {

    private final UserService userService;
    private final NutritionGoalClientService nutritionGoalClientService;
    private final NutritionServiceClient nutritionServiceClient;
    private final MealLogService mealLogService;

    public ComplianceCheckJob(UserService userService, NutritionGoalClientService nutritionGoalClientService, NutritionServiceClient nutritionServiceClient, MealLogService mealLogService) {
        this.userService = userService;
        this.nutritionGoalClientService = nutritionGoalClientService;
        this.nutritionServiceClient = nutritionServiceClient;
        this.mealLogService = mealLogService;
    }

    @Scheduled(cron = "0 5 0 * * *")
    public void runDailyComplianceCheck() {
        for (UserDto user : userService.getAllUsers()) {
            NutritionGoalResponse goal = nutritionGoalClientService.getGoal(user.getId());

            if (goal == null) {
                continue;
            }

            LocalDate yesterday = LocalDate.now().minusDays(1);
            int consumed = mealLogService.getTotalCaloriesForUserOnDate(user.getId(), yesterday);
            ComplianceCheckRequest  complianceCheckRequest= ComplianceCheckRequest.builder()
                    .externalUserId(user.getId())
                    .checkDate(yesterday)
                    .totalCaloriesConsumed(consumed)
                    .targetedCalories(goal.getDailyCalorieTarget()).build();

            nutritionServiceClient.recordComplianceCheck(complianceCheckRequest);
        }
    }
}
