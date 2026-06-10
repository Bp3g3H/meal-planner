package com.lubomirgeorgiev.meal_planner.repository.dish;

import com.lubomirgeorgiev.meal_planner.model.entity.dish.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DishRepository extends JpaRepository<Dish, UUID> {
}
