package com.lubomirgeorgiev.meal_planner.service.meal_log;

import com.lubomirgeorgiev.meal_planner.exception.DishNotFoundException;
import com.lubomirgeorgiev.meal_planner.exception.MealLogNotFoundException;
import com.lubomirgeorgiev.meal_planner.exception.UserAlreadyExistsException;
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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
        User user = userRepository.findById(userId).orElseThrow(() -> new UserAlreadyExistsException("User not found"));

        Dish dish = dishRepository.findById(mealFormRequest.getDishId()).orElseThrow(() -> new DishNotFoundException(mealFormRequest.getDishId()));

        MealLog mealLog = MealLog.builder()
                .user(user)
                .dish(dish)
                .mealType(mealFormRequest.getMealType())
                .portionSize(mealFormRequest.getPortionSize())
                .loggedInOn(LocalDateTime.now())
                .notes(mealFormRequest.getNotes())
                .build();

        mealLogRepository.save(mealLog);
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
        MealLog log = findByIdAndUser(id, userId);
        Dish dish = dishRepository.findById(mealFormRequest.getDishId()).orElseThrow(() -> new DishNotFoundException(mealFormRequest.getDishId()));

        log.setDish(dish);
        log.setMealType(mealFormRequest.getMealType());
        log.setPortionSize(mealFormRequest.getPortionSize());
        log.setLoggedInOn(LocalDateTime.now());
        log.setNotes(mealFormRequest.getNotes());
        mealLogRepository.save(log);
        return MealLogMapper.toMealLogDto(log);
    }

    public List<MealLogRepresentation> findByGroup(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
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
        mealLogRepository.delete(mealLog);
    }
}
