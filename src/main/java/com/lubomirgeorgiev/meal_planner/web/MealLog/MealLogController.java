package com.lubomirgeorgiev.meal_planner.web.MealLog;

import com.lubomirgeorgiev.meal_planner.model.dto.meal_log.MealFormRequest;
import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealPortionSize;
import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealType;
import com.lubomirgeorgiev.meal_planner.service.dish.DishService;
import com.lubomirgeorgiev.meal_planner.service.meal_log.MealLogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("meals")
public class MealLogController {

    private MealLogService mealLogService;
    private DishService dishService;

    public MealLogController(MealLogService mealLogService, DishService dishService) {
        this.mealLogService = mealLogService;
        this.dishService = dishService;
    }

    @GetMapping("log")
    public ModelAndView getLogPage() {
        ModelAndView modelAndView = new ModelAndView("log-meal");
        modelAndView.addObject("mealTypes", MealType.values());
        modelAndView.addObject("mealPortionSizes", MealPortionSize.values());
        modelAndView.addObject("mealLogFormRequest", MealFormRequest.builder().build());
        modelAndView.addObject("dishes", dishService.findAll());

        return modelAndView;
    }
}
