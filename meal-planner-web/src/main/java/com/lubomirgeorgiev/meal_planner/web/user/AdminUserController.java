package com.lubomirgeorgiev.meal_planner.web.user;

import com.lubomirgeorgiev.meal_planner.model.entity.user.UserRole;
import com.lubomirgeorgiev.meal_planner.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class AdminUserController {

    private final UserService userService;

    AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public ModelAndView users() {
        ModelAndView modelAndView = new ModelAndView("admin-users");
        modelAndView.addObject("users", userService.getAllUsers());

        return modelAndView;
    }

    @PostMapping("/admin/users/{id}/role")
    public ModelAndView changeRole(@PathVariable UUID id, @RequestParam UserRole newRole,
                             RedirectAttributes redirectAttributes) {
        userService.changeRole(id, newRole);
        redirectAttributes.addFlashAttribute("updated", true);
        return new ModelAndView("redirect:/admin/users");
    }
}
