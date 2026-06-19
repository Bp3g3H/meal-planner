package com.lubomirgeorgiev.meal_planner.mapper.user;

import com.lubomirgeorgiev.meal_planner.model.dto.user.UserRegisterRequest;
import com.lubomirgeorgiev.meal_planner.model.dto.user.UserDto;
import com.lubomirgeorgiev.meal_planner.model.entity.user.User;
import com.lubomirgeorgiev.meal_planner.model.entity.user.UserRole;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdOn(user.getCreatedOn())
                .build();
    }

    public static User toUserEntity(UserRegisterRequest registerDto) {
        return User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(registerDto.getPassword())
                .role(UserRole.USER)
                .createdOn(LocalDateTime.now())
                .build();
    }
}
