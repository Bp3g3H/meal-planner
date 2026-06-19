package com.lubomirgeorgiev.meal_planner.web;


import com.lubomirgeorgiev.meal_planner.exception.GroupNameTakenException;
import com.lubomirgeorgiev.meal_planner.exception.GroupNotFoundException;
import com.lubomirgeorgiev.meal_planner.exception.InvalidGroupPasswordException;
import com.lubomirgeorgiev.meal_planner.exception.UserAlreadyExistsException;
import com.lubomirgeorgiev.meal_planner.model.dto.user.UserRegisterRequest;
import com.lubomirgeorgiev.meal_planner.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    private final UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return  modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage(){
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().build();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("registerDto", userRegisterRequest);
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView getRegisterPage(@Valid @ModelAttribute UserRegisterRequest userRegisterRequest, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("register");
            return modelAndView;
        }

        try {
            userService.register(userRegisterRequest);
            return new ModelAndView("redirect:/login");
        }  catch(UserAlreadyExistsException e) {
            modelAndView.addObject("error", e.getMessage());
            modelAndView.setViewName("register");
            return modelAndView;
        } catch (GroupNameTakenException e) {
            bindingResult.rejectValue("groupName", "group.taken", e.getMessage());
            modelAndView.setViewName("register");
            return modelAndView;
        } catch (GroupNotFoundException e) {
            bindingResult.rejectValue("groupName", "group.notfound", e.getMessage());
            modelAndView.setViewName("register");
            return modelAndView;
        } catch (InvalidGroupPasswordException e) {
            bindingResult.rejectValue("groupPassword", "group.badpassword", e.getMessage());
            modelAndView.setViewName("register");
            return modelAndView;
        }

    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(){
        UserRegisterRequest userLoginRequest = UserRegisterRequest.builder().build();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("userLoginRequest", userLoginRequest);
        return modelAndView;
    }
//
//    @PostMapping("/login")
//    public ModelAndView getLoginPage(@Valid @ModelAttribute UserLoginRequest userLoginRequest, BindingResult bindingResult, HttpSession session) {
//        ModelAndView modelAndView = new ModelAndView();
//        if(bindingResult.hasErrors()){
//           modelAndView.setViewName("login");
//           return modelAndView;
//        }
//
//        UserDto user = userService.login(userLoginRequest);
//        session.setAttribute("user_id", user.getId());
//
//        return new ModelAndView("redirect:/home");
//    }
//
//
//

//
//    @GetMapping("/home")
//    public ModelAndView getHomePage(HttpSession session) {
//        UserDto userDto = userService.getById((UUID) session.getAttribute("user_id"));
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("home");
//        modelAndView.addObject("user", userDto);
//        return modelAndView;
//    }
//
//    @GetMapping("/logout")
//    public ModelAndView getLogoutPage(HttpSession session) {
//        session.invalidate();
//        return new ModelAndView("redirect:/");
//    }
}
