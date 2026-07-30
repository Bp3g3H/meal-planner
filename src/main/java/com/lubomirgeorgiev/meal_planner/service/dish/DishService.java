package com.lubomirgeorgiev.meal_planner.service.dish;

import com.lubomirgeorgiev.meal_planner.exception.DishNotFoundException;
import com.lubomirgeorgiev.meal_planner.exception.UserNotFoundException;
import com.lubomirgeorgiev.meal_planner.mapper.dish.DishMapper;
import com.lubomirgeorgiev.meal_planner.model.dto.dish.DishFormRequest;
import com.lubomirgeorgiev.meal_planner.model.dto.dish.DishDto;
import com.lubomirgeorgiev.meal_planner.model.entity.dish.Dish;
import com.lubomirgeorgiev.meal_planner.model.entity.user.User;
import com.lubomirgeorgiev.meal_planner.repository.dish.DishRepository;
import com.lubomirgeorgiev.meal_planner.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final UserRepository userRepository;

    @Autowired
    public DishService(DishRepository dishRepository,  UserRepository userRepository) {
        this.dishRepository = dishRepository;
        this.userRepository = userRepository;
    }

    public List<DishDto> findAll() {
        return dishRepository.findAllByOrderByNameAsc().stream().map(DishMapper::toDishDto).toList();
    }

    public DishFormRequest findByIdToFormDto(UUID id) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new DishNotFoundException(id));
        return DishMapper.toDishCreateRequest(dish);
    }

    public DishDto create(DishFormRequest dishCreateForm, UUID userId) {
        User user =  userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Dish dish = Dish.builder()
                .name(dishCreateForm.getName())
                .description(dishCreateForm.getDescription())
                .calories(dishCreateForm.getCalories())
                .category(dishCreateForm.getCategory())
                .imageUrl(dishCreateForm.getImageUrl())
                .createdBy(user)
                .createdOn(LocalDateTime.now())
                .build();
        dishRepository.save(dish);
        return DishMapper.toDishDto(dish);
    }

    public DishDto update(UUID id, DishFormRequest dishCreateRequest) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new DishNotFoundException(id));

        dish.setName(dishCreateRequest.getName());
        dish.setDescription(dishCreateRequest.getDescription());
        dish.setCalories(dishCreateRequest.getCalories());
        dish.setCategory(dishCreateRequest.getCategory());
        dish.setImageUrl(dishCreateRequest.getImageUrl());

        Dish savedDish = dishRepository.save(dish);
        return DishMapper.toDishDto(savedDish);
    }

    public void deleteById(UUID id) {
        dishRepository.deleteById(id);
    }
}
