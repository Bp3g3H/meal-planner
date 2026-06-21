package com.lubomirgeorgiev.meal_planner.web.MealLog;

import com.lubomirgeorgiev.meal_planner.model.dto.meal_log.MealFormRequest;
import com.lubomirgeorgiev.meal_planner.model.dto.meal_log.MealLogRepresentation;
import com.lubomirgeorgiev.meal_planner.model.dto.user.UserDto;
import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealPortionSize;
import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealType;
import com.lubomirgeorgiev.meal_planner.service.dish.DishService;
import com.lubomirgeorgiev.meal_planner.service.meal_log.MealLogService;
import com.lubomirgeorgiev.meal_planner.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/meals")
public class MealLogController {

    private MealLogService mealLogService;
    private DishService dishService;
    private UserService userService;

    public MealLogController(MealLogService mealLogService, DishService dishService, UserService userService) {
        this.mealLogService = mealLogService;
        this.dishService = dishService;
        this.userService = userService;
    }

    @GetMapping("/log")
    public ModelAndView getLogPage() {
        ModelAndView modelAndView = new ModelAndView("log-meal");
        modelAndView.addObject("mealTypes", MealType.values());
        modelAndView.addObject("mealPortionSizes", MealPortionSize.values());
        modelAndView.addObject("mealFormRequest", MealFormRequest.builder().build());
        modelAndView.addObject("dishes", dishService.findAll());

        return modelAndView;
    }

    @PostMapping("/log")
    public ModelAndView logMeal(@Valid @ModelAttribute MealFormRequest mealFormRequest, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("log-meal");
            modelAndView.addObject("mealTypes", MealType.values());
            modelAndView.addObject("mealPortionSizes", MealPortionSize.values());
            modelAndView.addObject("dishes", dishService.findAll());

            return modelAndView;
        }

        mealLogService.logMeal(mealFormRequest, (UUID) session.getAttribute("user_id"));
        return new ModelAndView("redirect:/meals/diary");
    }

    @GetMapping("/diary")
    public ModelAndView diary(HttpSession session) {
        UserDto currentUser = userService.getById((UUID) session.getAttribute("user_id"));
        List<MealLogRepresentation> logs = mealLogService.findByGroup(currentUser.getId());
        ModelAndView modelAndView = new ModelAndView("my-diary");
        modelAndView.addObject("mealLogs", logs);
        modelAndView.addObject("mealLogsByDate", mealLogService.groupByDate(logs));
        modelAndView.addObject("group", currentUser.getGroup());
        modelAndView.addObject("username", currentUser.getUsername());
        return modelAndView;
    }
}
