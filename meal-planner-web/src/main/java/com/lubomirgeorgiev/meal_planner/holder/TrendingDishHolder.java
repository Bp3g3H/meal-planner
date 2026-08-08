package com.lubomirgeorgiev.meal_planner.holder;

import com.lubomirgeorgiev.meal_planner.model.entity.dish.Dish;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class TrendingDishHolder {

    private final AtomicReference<Dish> trendingDish =
            new AtomicReference<>();

    public Dish getTrendingDish() {
        return trendingDish.get();
    }

    public void setTrendingDish(Dish dish) {
        trendingDish.set(dish);
    }
}
