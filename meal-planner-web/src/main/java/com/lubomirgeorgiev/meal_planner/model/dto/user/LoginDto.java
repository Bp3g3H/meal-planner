package com.lubomirgeorgiev.meal_planner.model.dto.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginDto {

    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
