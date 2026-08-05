package com.lubomirgeorgiev.meal_planner.client;

import com.lubomirgeorgiev.meal_planner.model.dto.compliance_check.ComplianceCheckRequest;
import com.lubomirgeorgiev.meal_planner.model.dto.compliance_check.WeeklyComplianceSummaryResponse;
import com.lubomirgeorgiev.meal_planner.model.dto.nutrition_goal.NutritionGoalRequest;
import com.lubomirgeorgiev.meal_planner.model.dto.nutrition_goal.NutritionGoalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "nutrition-service", url = "${nutrition.service.url}")
public interface NutritionServiceClient {

    @PostMapping("/nutrition-goals")
    NutritionGoalResponse createGoal(@RequestBody NutritionGoalRequest req);

    @PutMapping("/nutrition-goals")
    NutritionGoalResponse updateGoal(@RequestBody NutritionGoalRequest req);

    @GetMapping("/nutrition-goals/{userId}")
    NutritionGoalResponse getGoal(@PathVariable UUID userId);

    @PostMapping("/compliance-checks")
    void recordComplianceCheck(@RequestBody ComplianceCheckRequest req);

    @GetMapping("/compliance-checks/{userId}/weekly")
    WeeklyComplianceSummaryResponse getWeeklySummary(@PathVariable UUID userId);
}
