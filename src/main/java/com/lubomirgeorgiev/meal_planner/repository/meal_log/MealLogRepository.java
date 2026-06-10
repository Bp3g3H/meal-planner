package com.lubomirgeorgiev.meal_planner.repository.meal_log;

import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MealLogRepository extends JpaRepository<MealLog, UUID> {
}
