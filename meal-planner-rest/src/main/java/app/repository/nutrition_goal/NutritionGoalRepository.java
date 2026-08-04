package app.repository.nutrition_goal;

import app.model.entity.nutrition_goal.NutritionGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NutritionGoalRepository extends JpaRepository<NutritionGoal, UUID> {
    Optional<NutritionGoal> findByExternalUserId(UUID externalId);
    boolean existsByExternalUserId(UUID externalId);

}
