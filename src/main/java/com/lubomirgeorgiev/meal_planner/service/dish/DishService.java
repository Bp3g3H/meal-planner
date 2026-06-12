package com.lubomirgeorgiev.meal_planner.service.dish;

import com.lubomirgeorgiev.meal_planner.mapper.dish.DishMapper;
import com.lubomirgeorgiev.meal_planner.model.dto.dish.DishDto;
import com.lubomirgeorgiev.meal_planner.model.entity.dish.Dish;
import com.lubomirgeorgiev.meal_planner.model.entity.user.User;
import com.lubomirgeorgiev.meal_planner.repository.dish.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DishService {

    private final DishRepository dishRepository;

    @Autowired
    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<DishDto> findAll() {
        return dishRepository.findAll().stream().map(DishMapper::toDishDto).toList();
    }

    public DishDto findById(UUID id) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new RuntimeException("Dish not found"));
        return DishMapper.toDishDto(dish);
    }

    public DishDto create(DishDto dishDto, User user) {
        Dish dish = Dish.builder()
                .name(dishDto.getName())
                .description(dishDto.getDescription())
                .calories(dishDto.getCalories())
                .category(dishDto.getCategory())
                .imageUrl(dishDto.getImageUrl())
                .createdBy(user)
                .createdOn(LocalDateTime.now())
                .build();
        dishRepository.save(dish);
        return DishMapper.toDishDto(dish);
    }

    public DishDto update(UUID id, DishDto dishDto) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new RuntimeException("Dish not found"));

        dish.setName(dishDto.getName());
        dish.setDescription(dishDto.getDescription());
        dish.setCalories(dishDto.getCalories());
        dish.setCategory(dishDto.getCategory());
        dish.setImageUrl(dishDto.getImageUrl());

        Dish savedDish = dishRepository.save(dish);
        return DishMapper.toDishDto(savedDish);
    }

    public void deleteById(UUID id) {
        dishRepository.deleteById(id);
    }
}
