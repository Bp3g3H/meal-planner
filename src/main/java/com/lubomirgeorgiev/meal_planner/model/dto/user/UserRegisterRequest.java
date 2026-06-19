package com.lubomirgeorgiev.meal_planner.model.dto.user;

import com.lubomirgeorgiev.meal_planner.model.entity.user.GroupChoice;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterRequest {

    @NotBlank
    @Size(min = 3, max = 30)
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8)
    private String password;
    @NotBlank
    private String confirmPassword;
    @NotNull
    private GroupChoice groupChoice;
    @Size(max = 30)
    private String groupName;
    @Size(max = 20)
    private String groupPassword;
}
