package com.lubomirgeorgiev.meal_planner.web.group;

import com.lubomirgeorgiev.meal_planner.model.dto.group.GroupDto;
import com.lubomirgeorgiev.meal_planner.model.dto.group.GroupUpgradeDto;
import com.lubomirgeorgiev.meal_planner.model.dto.user.UserDto;
import com.lubomirgeorgiev.meal_planner.service.group.GroupService;
import com.lubomirgeorgiev.meal_planner.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;

    @Autowired
    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getGroupSettingsPage(HttpSession session) {
        UserDto userDto = userService.getById((UUID)  session.getAttribute("user_id"));
        GroupDto groupDto = userDto.getGroup();
        ModelAndView modelAndView = new ModelAndView("group-settings");
        modelAndView.addObject("groupDto", groupDto);

        if (groupDto.isDummy()) {
            modelAndView.addObject("groupUpgradeDto", GroupUpgradeDto.builder().build());
        }

        return modelAndView;
    }
}
