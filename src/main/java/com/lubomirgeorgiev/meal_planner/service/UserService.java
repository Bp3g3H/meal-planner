package com.lubomirgeorgiev.meal_planner.service;

import com.lubomirgeorgiev.meal_planner.mapper.user.UserMapper;
import com.lubomirgeorgiev.meal_planner.model.dto.user.RegisterDto;
import com.lubomirgeorgiev.meal_planner.model.dto.user.UserDto;
import com.lubomirgeorgiev.meal_planner.model.entity.user.User;
import com.lubomirgeorgiev.meal_planner.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public UserDto register (RegisterDto registerDto) {
        userRepository.findByUsername(registerDto.getUsername()).ifPresent(user -> {
            throw new RuntimeException("Username is already in use");
        });

        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        User userEntity = UserMapper.toUserEntity(registerDto);

        userRepository.save(userEntity);
        return UserMapper.toUserDto(userEntity);
    }
}
