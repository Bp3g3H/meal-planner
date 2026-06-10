package com.lubomirgeorgiev.meal_planner.model.entity.meal_log;

import com.lubomirgeorgiev.meal_planner.model.entity.dish.Dish;
import com.lubomirgeorgiev.meal_planner.model.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "meal_log")
public class MealLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    private Dish dish;
    @Enumerated(EnumType.STRING)
    private MealType mealType;
    private LocalDateTime loggedInOn;
    private String notes;
    @Enumerated(EnumType.STRING)
    private PortionSize portionSize;
}
