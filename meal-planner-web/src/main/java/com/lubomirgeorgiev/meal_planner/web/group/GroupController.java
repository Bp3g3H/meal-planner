package com.lubomirgeorgiev.meal_planner.web.group;

import com.lubomirgeorgiev.meal_planner.exception.GroupNameTakenException;
import com.lubomirgeorgiev.meal_planner.model.dto.group.GroupDto;
import com.lubomirgeorgiev.meal_planner.model.dto.group.GroupUpgradeDto;
import com.lubomirgeorgiev.meal_planner.model.dto.user.UserDto;
import com.lubomirgeorgiev.meal_planner.service.group.GroupService;
import com.lubomirgeorgiev.meal_planner.service.group.GroupUpgradeResult;
import com.lubomirgeorgiev.meal_planner.service.user.AuthenticationUserDetails;
import com.lubomirgeorgiev.meal_planner.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ModelAndView getGroupSettingsPage(@AuthenticationPrincipal AuthenticationUserDetails user) {
        UserDto userDto = userService.getById(user.getId());
        GroupDto groupDto = userDto.getGroup();
        ModelAndView modelAndView = new ModelAndView("group-settings");
        modelAndView.addObject("groupDto", groupDto);

        if (groupDto.isDummy()) {
            modelAndView.addObject("groupUpgradeDto", GroupUpgradeDto.builder().build());
        }

        return modelAndView;
    }

    @PostMapping("/upgrade")
    public ModelAndView upgrade(
            @Valid @ModelAttribute("groupUpgradeDto") GroupUpgradeDto groupUpgradeDto,
            BindingResult result,
            @AuthenticationPrincipal AuthenticationUserDetails user,
            RedirectAttributes redirectAttributes
    ) {
        UserDto userDto = userService.getById(user.getId());
        GroupDto groupDto = userDto.getGroup();
        if (result.hasErrors()) {

            ModelAndView modelAndView = new ModelAndView("group-settings");
            modelAndView.addObject("groupDto", groupDto);
            return modelAndView;
        }

        try {
            GroupUpgradeResult upgradeResult = groupService.upgradeDummyGroup(
                    groupDto.getId(),
                    groupUpgradeDto,
                    userDto.getId());

            if (upgradeResult.rawCode() != null) {
                redirectAttributes.addFlashAttribute("rawJoinCode", upgradeResult.rawCode());
            }
        } catch (GroupNameTakenException ex) {
            result.rejectValue("name", "group.taken", ex.getMessage());
            ModelAndView modelAndView = new ModelAndView("group-settings");
            modelAndView.addObject("groupDto", groupDto);
            return modelAndView;
        }

        return new ModelAndView("redirect:/group");
    }
}
