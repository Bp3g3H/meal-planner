package app.mapper.nutrition_goal;

import app.model.dto.nutrition_goal.NutritionGoalResponse;
import app.model.entity.nutrition_goal.NutritionGoal;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NutritionGoalMapper {

    public static NutritionGoalResponse toNutritionGoalResponse(NutritionGoal nutritionGoal) {
        return NutritionGoalResponse.builder()
                .id(nutritionGoal.getId())
                .externalUserId(nutritionGoal.getExternalUserId())
                .dailyCalorieTarget(nutritionGoal.getDailyCalorieTarget())
                .updatedOn(nutritionGoal.getUpdatedOn())
                .build();
    }
}
