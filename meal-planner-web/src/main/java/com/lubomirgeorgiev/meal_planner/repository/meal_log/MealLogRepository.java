package com.lubomirgeorgiev.meal_planner.repository.meal_log;

import com.lubomirgeorgiev.meal_planner.model.entity.dish.Dish;
import com.lubomirgeorgiev.meal_planner.model.entity.group.Group;
import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealLog;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MealLogRepository extends JpaRepository<MealLog, UUID> {
    List<MealLog> findByUserGroupOrderByLoggedInOnDesc(Group group);
    List<MealLog> findByUser_IdAndLoggedInOnBetween(UUID userId, LocalDateTime loggedInOnAfter, LocalDateTime loggedInOnBefore);

    @Query("""
        SELECT ml.dish
        FROM MealLog ml
        WHERE ml.loggedInOn >= :from
        GROUP BY ml.dish
        ORDER BY COUNT(ml) DESC
    """)
    List<Dish> findMostPopularDish(@Param("from") LocalDateTime from);
}
