package com.lubomirgeorgiev.meal_planner.web.nutrition_goal;

import com.lubomirgeorgiev.meal_planner.model.dto.nutrition_goal.NutritionGoalFormDto;
import com.lubomirgeorgiev.meal_planner.model.dto.nutrition_goal.NutritionGoalResponse;
import com.lubomirgeorgiev.meal_planner.service.nutrition.NutritionGoalClientService;
import com.lubomirgeorgiev.meal_planner.service.user.AuthenticationUserDetails;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/nutrition-goals")
public class NutritionGoalController {

    private final NutritionGoalClientService nutritionGoalClientService;

    public NutritionGoalController(NutritionGoalClientService nutritionGoalClientService) {
        this.nutritionGoalClientService = nutritionGoalClientService;
    }

    @GetMapping
    public ModelAndView view(@AuthenticationPrincipal AuthenticationUserDetails currentUser) {
        NutritionGoalResponse goal = nutritionGoalClientService.getGoal(currentUser.getId());
        NutritionGoalFormDto dto = NutritionGoalFormDto.builder()
                .dailyCalorieTarget(goal.getDailyCalorieTarget())
                .build();

        ModelAndView modelAndView = new ModelAndView("nutrition-goal");
        modelAndView.addObject("goal", goal);
        modelAndView.addObject("dto", dto);

        return modelAndView;
    }

    @PostMapping
    public ModelAndView save(@Valid @ModelAttribute("goalDto") NutritionGoalFormDto dto,
                       BindingResult result, @AuthenticationPrincipal AuthenticationUserDetails currentUser,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("nutrition-goal");
            modelAndView.addObject("dto", dto);
            return modelAndView;
        }

        ModelAndView modelAndView = new ModelAndView("redirect:/nutrition-goal");
        nutritionGoalClientService.saveGoal(currentUser.getId(), dto.getDailyCalorieTarget());
        redirectAttributes.addFlashAttribute("saved", true);
        return modelAndView;
    }
}
