package com.lubomirgeorgiev.meal_planner.web.MealLog;

import com.lubomirgeorgiev.meal_planner.exception.DishNotFoundException;
import com.lubomirgeorgiev.meal_planner.exception.MealLogNotFoundException;
import com.lubomirgeorgiev.meal_planner.model.dto.meal_log.MealFormRequest;
import com.lubomirgeorgiev.meal_planner.model.dto.meal_log.MealLogRepresentation;
import com.lubomirgeorgiev.meal_planner.model.dto.user.UserDto;
import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealPortionSize;
import com.lubomirgeorgiev.meal_planner.model.entity.meal_log.MealType;
import com.lubomirgeorgiev.meal_planner.service.dish.DishService;
import com.lubomirgeorgiev.meal_planner.service.meal_log.MealLogService;
import com.lubomirgeorgiev.meal_planner.service.user.AuthenticationUserDetails;
import com.lubomirgeorgiev.meal_planner.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    public ModelAndView logMeal(@Valid @ModelAttribute MealFormRequest mealFormRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationUserDetails user) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("log-meal");
            modelAndView.addObject("mealTypes", MealType.values());
            modelAndView.addObject("mealPortionSizes", MealPortionSize.values());
            modelAndView.addObject("dishes", dishService.findAll());

            return modelAndView;
        }

        mealLogService.logMeal(mealFormRequest, user.getId());
        return new ModelAndView("redirect:/meals/diary");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView getEditLogPage(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationUserDetails user) {
        ModelAndView modelAndView = new ModelAndView("edit-meal");
        modelAndView.addObject("mealFormRequest", mealLogService.getMealFormRequestByIdAndUser(id, user.getId()));
        modelAndView.addObject("dishes", dishService.findAll());
        modelAndView.addObject("mealLogId", id);
        modelAndView.addObject("mealTypes", MealType.values());
        modelAndView.addObject("mealPortionSizes", MealPortionSize.values());
        return modelAndView;
    }

    @PostMapping("/{id}/edit")
    public ModelAndView editMeal(
            @PathVariable UUID id,
            @Valid @ModelAttribute("mealFormRequest") MealFormRequest mealFormRequest,
            BindingResult result,
            @AuthenticationPrincipal AuthenticationUserDetails user) {


        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("edit-meal");
            modelAndView.addObject("dishes", dishService.findAll());
            modelAndView.addObject("mealLogId", id);
            modelAndView.addObject("mealTypes", MealType.values());
            modelAndView.addObject("mealPortionSizes", MealPortionSize.values());
            return modelAndView;
        }

        try {
            mealLogService.updateMeal(id, mealFormRequest, user.getId());
        } catch (DishNotFoundException ex) {
            result.rejectValue("dishId", "dish.notfound", ex.getMessage());
            ModelAndView modelAndView = new ModelAndView("edit-meal");
            modelAndView.addObject("dishes", dishService.findAll());
            modelAndView.addObject("mealLogId", id);
            modelAndView.addObject("mealTypes", MealType.values());
            modelAndView.addObject("mealPortionSizes", MealPortionSize.values());
            return modelAndView;
        } catch (MealLogNotFoundException ex) {
            throw ex;
        }

        return new ModelAndView("redirect:/meals/diary");
    }

    @GetMapping("/diary")
    public ModelAndView diary(@AuthenticationPrincipal AuthenticationUserDetails user) {
        UserDto currentUser = userService.getById(user.getId());
        List<MealLogRepresentation> logs = mealLogService.findByGroup(currentUser.getId());
        ModelAndView modelAndView = new ModelAndView("my-diary");
        modelAndView.addObject("mealLogs", logs);
        modelAndView.addObject("mealLogsByDate", mealLogService.groupByDate(logs));
        modelAndView.addObject("group", currentUser.getGroup());
        modelAndView.addObject("username", currentUser.getUsername());
        modelAndView.addObject("currentUserId", currentUser.getId());
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationUserDetails user) {
        mealLogService.delete(id, user.getId());
        return "redirect:/meals/diary";
    }
}
