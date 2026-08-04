package app.service.nutrition_goal;

import app.mapper.nutrition_goal.NutritionGoalMapper;
import app.model.dto.nutrition_goal.NutritionGoalRequest;
import app.model.dto.nutrition_goal.NutritionGoalResponse;
import app.model.entity.nutrition_goal.NutritionGoal;
import app.repository.nutrition_goal.NutritionGoalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class NutritionGoalService {

    private NutritionGoalRepository nutritionGoalRepository;

    public NutritionGoalService(NutritionGoalRepository nutritionGoalRepository) {
        this.nutritionGoalRepository = nutritionGoalRepository;
    }

    public NutritionGoalResponse create(NutritionGoalRequest nutritionGoalRequest) {
        if (nutritionGoalRepository.existsByExternalUserId(nutritionGoalRequest.getExternalUserId())) {
            // TODO CHANGE LATER TO CUSTOM EXCEPTION
            throw new IllegalArgumentException("Nutrition goal already exists for this user");
        }

        NutritionGoal nutritionGoal = NutritionGoal.builder()
                .externalUserId(nutritionGoalRequest.getExternalUserId())
                .dailyCalorieTarget(nutritionGoalRequest.getDailyCalorieTarget())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        nutritionGoalRepository.save(nutritionGoal);

        return NutritionGoalMapper.toNutritionGoalResponse(nutritionGoal);
    }

    public NutritionGoalResponse update(NutritionGoalRequest nutritionGoalRequest) {
        NutritionGoal nutritionGoal = nutritionGoalRepository.findByExternalUserId(nutritionGoalRequest.getExternalUserId())
                // TODO CHANGE LATER TO CUSTOM EXCEPTION
                .orElseThrow(() -> new IllegalArgumentException("Nutrition goal not found for this user"));

        nutritionGoal.setDailyCalorieTarget(nutritionGoalRequest.getDailyCalorieTarget());
        nutritionGoalRepository.save(nutritionGoal);

        return NutritionGoalMapper.toNutritionGoalResponse(nutritionGoal);
    }

    public NutritionGoalResponse findByExternalUserId(UUID externalUserId) {
        NutritionGoal nutritionGoal = nutritionGoalRepository.findByExternalUserId(externalUserId)
                // TODO CHANGE LATER TO CUSTOM EXCEPTION
                .orElseThrow(() -> new IllegalArgumentException("Nutrition goal not found for this user"));

        return NutritionGoalMapper.toNutritionGoalResponse(nutritionGoal);
    }
}
