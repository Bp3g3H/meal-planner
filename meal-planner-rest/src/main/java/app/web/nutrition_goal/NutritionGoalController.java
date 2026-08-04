package app.web.nutrition_goal;

import app.model.dto.nutrition_goal.NutritionGoalRequest;
import app.model.dto.nutrition_goal.NutritionGoalResponse;
import app.service.nutrition_goal.NutritionGoalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/nutrition-goals")
public class NutritionGoalController {

    private final NutritionGoalService nutritionGoalService;

    public NutritionGoalController(NutritionGoalService nutritionGoalService) {
        this.nutritionGoalService = nutritionGoalService;
    }

    @PostMapping
    public ResponseEntity<NutritionGoalResponse> create(@Valid @RequestBody NutritionGoalRequest nutritionGoalRequest) {
        NutritionGoalResponse response = nutritionGoalService.create(nutritionGoalRequest);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<NutritionGoalResponse> update(@Valid @RequestBody NutritionGoalRequest nutritionGoalRequest) {
        NutritionGoalResponse response = nutritionGoalService.update(nutritionGoalRequest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<NutritionGoalResponse> findByUserId(@PathVariable UUID userId) {
        NutritionGoalResponse response = nutritionGoalService.findByExternalUserId(userId);

        return ResponseEntity.ok(response);
    }
}
