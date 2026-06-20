package com.lubomirgeorgiev.meal_planner.web.dish;

import com.lubomirgeorgiev.meal_planner.service.dish.DishService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/dishes")
public class DishController {

    private DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public ModelAndView catalog() {
        ModelAndView modelAndView = new ModelAndView("dish-catalog");
        modelAndView.addObject("dishes", dishService.findAll());
        return modelAndView;
    }

}
