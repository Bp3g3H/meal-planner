package com.lubomirgeorgiev.meal_planner.web.user;

import com.lubomirgeorgiev.meal_planner.model.dto.user.ProfileUpdateDto;
import com.lubomirgeorgiev.meal_planner.service.user.AuthenticationUserDetails;
import com.lubomirgeorgiev.meal_planner.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ModelAndView view(@AuthenticationPrincipal AuthenticationUserDetails user) {
        ModelAndView modelAndView = new ModelAndView("profile");
        ProfileUpdateDto profileUpdateDto = ProfileUpdateDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();

       modelAndView.addObject("profileUpdateDto", profileUpdateDto);
        return modelAndView;
    }

    @PostMapping("/profile")
    public ModelAndView update(@Valid @ModelAttribute("profileUpdateDto") ProfileUpdateDto profileUpdateDto,
                         BindingResult result, @AuthenticationPrincipal AuthenticationUserDetails user,
                         RedirectAttributes redirectAttributes) {


        if (result.hasErrors()) {
            return new ModelAndView("profile");
        }

        userService.updateProfile(user.getId(), profileUpdateDto);
        redirectAttributes.addFlashAttribute("updated", true);
        return new ModelAndView("redirect:/profile");
    }
}
