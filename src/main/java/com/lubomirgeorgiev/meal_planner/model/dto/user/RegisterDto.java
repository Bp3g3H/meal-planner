package com.lubomirgeorgiev.meal_planner.model.dto.user;

import com.lubomirgeorgiev.meal_planner.model.entity.user.GroupChoice;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDto {

    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private GroupChoice groupChoice;
    private String groupName;
    private String groupPassword;
}
