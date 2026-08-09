package com.lubomirgeorgiev.meal_planner.service.nutrition;

import com.lubomirgeorgiev.meal_planner.model.dto.compliance_check.WeeklyComplianceSummaryResponse;
import com.lubomirgeorgiev.meal_planner.service.nutrition.client.NutritionServiceClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NutritionInsightsService {
    private final NutritionServiceClient nutritionServiceClient;

    public NutritionInsightsService(NutritionServiceClient nutritionServiceClient) {
        this.nutritionServiceClient = nutritionServiceClient;
    }

    public WeeklyComplianceSummaryResponse getWeeklySummary(UUID userId) {
        return  nutritionServiceClient.getWeeklySummary(userId);
    }
}
