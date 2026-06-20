package com.lubomirgeorgiev.meal_planner.web.dish;

import com.lubomirgeorgiev.meal_planner.model.dto.dish.DishDto;
import com.lubomirgeorgiev.meal_planner.model.entity.dish.DishCategory;
import com.lubomirgeorgiev.meal_planner.service.dish.DishService;
import com.lubomirgeorgiev.meal_planner.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class DishController {

    private DishService dishService;
    private UserService userService;

    public DishController(DishService dishService,  UserService userService) {
        this.dishService = dishService;
        this.userService = userService;
    }

    @GetMapping("/dishes")
    public ModelAndView catalog() {
        ModelAndView modelAndView = new ModelAndView("dish-catalog");
        modelAndView.addObject("dishes", dishService.findAll());
        return modelAndView;
    }

    @GetMapping("/admin/dishes")
    public ModelAndView adminCatalog() {
        ModelAndView modelAndView = new ModelAndView("admin-dishes");
        modelAndView.addObject("dishes", dishService.findAll());
        return modelAndView;
    }

    @GetMapping("/admin/dishes/add")
    public ModelAndView getAddForm() {
        ModelAndView modelAndView = new ModelAndView("admin-dish-form");
        modelAndView.addObject("dishDto", DishDto.builder().build());
        modelAndView.addObject("dishCategories", DishCategory.values());
        return modelAndView;
    }

    @PostMapping("/admin/add")
    public ModelAndView createDish(@Valid @ModelAttribute DishDto dishDto, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin-dish-form");
        }

        dishService.create(dishDto, (UUID) session.getAttribute("user_id"));
        return new ModelAndView("redirect:/admin/dishes");
    }



}
