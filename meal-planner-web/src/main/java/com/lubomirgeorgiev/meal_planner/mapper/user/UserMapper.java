package com.lubomirgeorgiev.meal_planner.mapper.user;

import com.lubomirgeorgiev.meal_planner.mapper.group.GroupMapper;
import com.lubomirgeorgiev.meal_planner.model.dto.user.UserDto;
import com.lubomirgeorgiev.meal_planner.model.entity.user.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdOn(user.getCreatedOn())
                .group(GroupMapper.toGroupDto(user.getGroup()))
                .build();
    }
}
