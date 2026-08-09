package com.lubomirgeorgiev.meal_planner.service.meal_log;

import com.lubomirgeorgiev.meal_planner.exception.DishNotFoundException;
import com.lubomirgeorgiev.meal_planner.exception.MealLogNotFoundException;
import com.lubomirgeorgiev.meal_planner.exception.UserNotFoundException;
import com.lubomirgeorgiev.meal_planner.mapper.meal_log.MealLogMapper;
import com.lubomirgeorgiev.meal_planner.model.dto.meal_log.MealFormRequest;
import com.lubomirgeorgiev.meal_planner.model.dto.meal_log.MealLogDto;
import com.lubomirgeorgiev.meal_planner.model.dto.meal_log.MealLogRepresentation;
import com.lubomirgeorgiev.meal_planner.model.entity.dish.Dish;
import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealLog;
import com.lubomirgeorgiev.meal_planner.model.entity.user.User;
import com.lubomirgeorgiev.meal_planner.repository.dish.DishRepository;
import com.lubomirgeorgiev.meal_planner.repository.meal_log.MealLogRepository;
import com.lubomirgeorgiev.meal_planner.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
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

    public MealLogDto logMeal(MealFormRequest mealFormRequest, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Dish dish = dishRepository.findById(mealFormRequest.getDishId()).orElseThrow(() -> new DishNotFoundException(mealFormRequest.getDishId()));

        MealLog mealLog = MealLog.builder()
                .user(user)
                .dish(dish)
                .mealType(mealFormRequest.getMealType())
                .portionSize(mealFormRequest.getPortionSize())
                .loggedInOn(mealFormRequest.getLoggedInOn().atTime(LocalTime.now()))
                .notes(mealFormRequest.getNotes())
                .build();

        mealLogRepository.save(mealLog);

        log.info("User {} logged meal for dish {}", userId, dish.getId());

        return MealLogMapper.toMealLogDto(mealLog);
    }

    public MealLog findByIdAndUser(UUID id, UUID userId) {
        MealLog log = mealLogRepository.findById(id).orElseThrow(() -> new MealLogNotFoundException(id));

        if (!log.getUser().getId().equals(userId)) {
            throw new MealLogNotFoundException(log.getId());
        }

        return log;
    }

    public MealFormRequest getMealFormRequestByIdAndUser(UUID id, UUID userId) {
        MealLog log = mealLogRepository.findById(id).orElseThrow(() -> new MealLogNotFoundException(id));
        return MealLogMapper.toMealFormRequest(log);
    }

    public MealLogDto updateMeal(UUID id, MealFormRequest mealFormRequest, UUID userId) {
        MealLog mealLog = findByIdAndUser(id, userId);
        Dish dish = dishRepository.findById(mealFormRequest.getDishId()).orElseThrow(() -> new DishNotFoundException(mealFormRequest.getDishId()));

        mealLog.setDish(dish);
        mealLog.setMealType(mealFormRequest.getMealType());
        mealLog.setPortionSize(mealFormRequest.getPortionSize());
        mealLog.setLoggedInOn(LocalDateTime.now());
        mealLog.setNotes(mealFormRequest.getNotes());

        mealLogRepository.save(mealLog);

        log.info("User {} updated meal log {}", userId, id);

        return MealLogMapper.toMealLogDto(mealLog);
    }

    public List<MealLogRepresentation> findByGroup(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<MealLog> logs = mealLogRepository.findByUserGroupOrderByLoggedInOnDesc(user.getGroup());

        return logs.stream().map(MealLogMapper::toRepresentation).toList();
    }

    public Map<LocalDate, List<MealLogRepresentation>> groupByDate(List<MealLogRepresentation> logs) {
        return logs.stream()
                .collect(Collectors.groupingBy(
                        MealLogRepresentation::getLoggedInOn,
                        LinkedHashMap::new,
                        Collectors.toList()));
    }

    public void delete(UUID id, UUID userId) {
        MealLog mealLog = findByIdAndUser(id, userId);

        log.info("User {} deleted meal log {}", userId, id);

        mealLogRepository.delete(mealLog);
    }

    public int getTotalCaloriesForUserOnDate(UUID userId, LocalDate date) {
        return mealLogRepository.findByUser_IdAndLoggedInOn(userId, date).stream().mapToInt(log ->
             Math.toIntExact(Math.round(log.getDish().getCalories() * log.getPortionSize().getMultiplier()))
        ).sum();
    }

    public Dish getMostPopularDishForWeek() {

        LocalDateTime from = LocalDateTime.now().minusDays(7);

        return mealLogRepository
                .findMostPopularDish(from)
                .stream()
                .findFirst()
                .orElse(null);
    }

}
