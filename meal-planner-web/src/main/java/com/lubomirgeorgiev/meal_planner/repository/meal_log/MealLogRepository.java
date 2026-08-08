package com.lubomirgeorgiev.meal_planner.repository.meal_log;

import com.lubomirgeorgiev.meal_planner.model.entity.group.Group;
import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MealLogRepository extends JpaRepository<MealLog, UUID> {
    List<MealLog> findByUserGroupOrderByLoggedInOnDesc(Group group);
    List<MealLog> findByUser_IdAndLoggedIn(UUID userId, LocalDate date);
}
