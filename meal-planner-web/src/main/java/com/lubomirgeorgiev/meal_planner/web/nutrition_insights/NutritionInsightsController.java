package com.lubomirgeorgiev.meal_planner.web.nutrition_insights;

import com.lubomirgeorgiev.meal_planner.holder.TrendingDishHolder;
import com.lubomirgeorgiev.meal_planner.service.nutrition.NutritionInsightsService;
import com.lubomirgeorgiev.meal_planner.service.user.AuthenticationUserDetails;
import feign.FeignException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NutritionInsightsController {

    private final NutritionInsightsService nutritionInsightsService;
    private final TrendingDishHolder trendingDishHolder;

    public NutritionInsightsController(NutritionInsightsService nutritionInsightsService, TrendingDishHolder trendingDishHolder) {
        this.nutritionInsightsService = nutritionInsightsService;
        this.trendingDishHolder = trendingDishHolder;
    }

    @GetMapping("/nutrition-insights")
    public ModelAndView view(@AuthenticationPrincipal AuthenticationUserDetails user) {
        ModelAndView modelAndView = new ModelAndView("nutrition-insights");

        modelAndView.addObject("trendingDish", trendingDishHolder.getTrendingDish());

        try {
            modelAndView.addObject("summary", nutritionInsightsService.getWeeklySummary(user.getId()));
        } catch (FeignException.NotFound e) {
            modelAndView.addObject("summary", null);
        } catch (FeignException e) {
            modelAndView.addObject("serviceUnavailable", true);
        }

        return modelAndView;
    }
}
