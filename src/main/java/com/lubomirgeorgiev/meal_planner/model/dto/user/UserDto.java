package com.lubomirgeorgiev.meal_planner.model.dto.user;

import com.lubomirgeorgiev.meal_planner.model.entity.user.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class UserDto {

    private UUID id;
    private String username;
    private String email;
    private UserRole role;
    private LocalDateTime createdOn;
}
