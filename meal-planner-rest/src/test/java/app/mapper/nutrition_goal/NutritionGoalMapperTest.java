package app.mapper.nutrition_goal;

import app.model.dto.nutrition_goal.NutritionGoalResponse;
import app.model.entity.nutrition_goal.NutritionGoal;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class NutritionGoalMapperTest {

    @Test
    void toNutritionGoalResponse_mapsAllFields() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime updatedOn = LocalDateTime.now();

        NutritionGoal goal = NutritionGoal.builder()
                .id(id)
                .externalUserId(userId)
                .dailyCalorieTarget(2200)
                .createdOn(updatedOn.minusDays(3))
                .updatedOn(updatedOn)
                .build();

        NutritionGoalResponse response = NutritionGoalMapper.toNutritionGoalResponse(goal);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getExternalUserId()).isEqualTo(userId);
        assertThat(response.getDailyCalorieTarget()).isEqualTo(2200);
        assertThat(response.getUpdatedOn()).isEqualTo(updatedOn);
    }
}
