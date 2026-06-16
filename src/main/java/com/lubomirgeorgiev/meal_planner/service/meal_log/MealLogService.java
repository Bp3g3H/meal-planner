package com.lubomirgeorgiev.meal_planner.service.meal_log;

import com.lubomirgeorgiev.meal_planner.model.dto.meal_log.MealLogDto;
import com.lubomirgeorgiev.meal_planner.model.entity.dish.Dish;
import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealLog;
import com.lubomirgeorgiev.meal_planner.model.entity.user.User;
import com.lubomirgeorgiev.meal_planner.repository.dish.DishRepository;
import com.lubomirgeorgiev.meal_planner.repository.meal_log.MealLogRepository;
import com.lubomirgeorgiev.meal_planner.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MealLogService {

    private final UserRepository userRepository;
    private final MealLogRepository mealLogRepository;
    private final DishRepository dishRepository;

    public MealLogService(UserRepository userRepository, MealLogRepository mealLogRepository, DishRepository dishRepository) {
        this.userRepository = userRepository;
        this.mealLogRepository = mealLogRepository;
        this.dishRepository = dishRepository;
    }

    public MealLog logMeal(MealLogDto mealLogDto, UUID userId) {
        // TODO change exception
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // TODO change DishNotFoundException
        Dish dish = dishRepository.findById(mealLogDto.getDishId()).orElseThrow(() -> new RuntimeException("Dish not found"));

        MealLog mealLog = MealLog.builder()
                .user(user)
                .dish(dish)
                .mealType(mealLogDto.getMealType())
                .portionSize(mealLogDto.getPortionSize())
                .loggedInOn(mealLogDto.getLoggedInOn())
                .notes(mealLogDto.getNotes())
                .build();

        mealLogRepository.save(mealLog);
        return mealLog;
    }

    public MealLog findByIdAndUser(UUID id, UUID userId) {
        // TODO change MealLogNotFoundException
        MealLog log = mealLogRepository.findById(id).orElseThrow(() -> new RuntimeException("Meal Log not found"));

        if (!log.getUser().getId().equals(userId)) {
            // TODO change MealLogNotFoundException
            throw new RuntimeException("Meal Log not found");
        }

        return log;
    }

    public MealLog updateMeal(UUID id, MealLogDto mealLogDto, UUID userId) {
        MealLog log = findByIdAndUser(id, userId);
        // TODO change DishNotFoundException
        Dish dish = dishRepository.findById(mealLogDto.getDishId()).orElseThrow(() -> new RuntimeException("Dish not found"));

        log.setDish(dish);
        log.setMealType(mealLogDto.getMealType());
        log.setPortionSize(mealLogDto.getPortionSize());
        log.setLoggedInOn(mealLogDto.getLoggedInOn());
        log.setNotes(mealLogDto.getNotes());
        mealLogRepository.save(log);
        return log;
    }

    public List<MealLog> findByGroup(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<MealLog> logs = mealLogRepository.findByUserGroupOrderByLoggedInOnDesc(user.getGroup());

        return logs;
    }

    public void delete(UUID id, UUID userId) {
        MealLog mealLog = findByIdAndUser(id, userId);
        mealLogRepository.delete(mealLog);
    }

    //TODO add getSummaryForGroup
}
