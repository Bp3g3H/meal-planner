package com.lubomirgeorgiev.meal_planner.service.user;

import com.lubomirgeorgiev.meal_planner.mapper.user.UserMapper;
import com.lubomirgeorgiev.meal_planner.model.dto.user.RegisterDto;
import com.lubomirgeorgiev.meal_planner.model.dto.user.UserDto;
import com.lubomirgeorgiev.meal_planner.model.entity.group.Group;
import com.lubomirgeorgiev.meal_planner.model.entity.user.GroupChoice;
import com.lubomirgeorgiev.meal_planner.model.entity.user.User;
import com.lubomirgeorgiev.meal_planner.model.entity.user.UserRole;
import com.lubomirgeorgiev.meal_planner.repository.user.UserRepository;
import com.lubomirgeorgiev.meal_planner.service.group.GroupService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private GroupService groupService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, GroupService groupService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.groupService = groupService;
    }


    public UserDto register (RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail()) || userRepository.existsByUsername(registerDto.getUsername())) {
            //TODO change to UserAlreadyExistsException
            throw new RuntimeException("User already exists");
        }

        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        Group group = null;
        if (registerDto.getGroupChoice().equals(GroupChoice.CREATE)) {
            group = groupService.createGroup(registerDto.getGroupName(), registerDto.getGroupPassword());
        } else if (registerDto.getGroupChoice().equals(GroupChoice.JOIN)) {
            group = groupService.joinGroup(registerDto.getGroupName(), registerDto.getGroupPassword());
        } else if (registerDto.getGroupChoice().equals(GroupChoice.NONE)) {
            group = groupService.createDummyGroup();
        }

        User user = User.builder()
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .email(registerDto.getEmail())
                .role(UserRole.USER)
                .group(group)
                .build();

        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }
}
