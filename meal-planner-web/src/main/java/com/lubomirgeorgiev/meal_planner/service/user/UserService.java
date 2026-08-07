package com.lubomirgeorgiev.meal_planner.service.user;

import com.lubomirgeorgiev.meal_planner.exception.PasswordDoesNotMatchException;
import com.lubomirgeorgiev.meal_planner.exception.UserAlreadyExistsException;
import com.lubomirgeorgiev.meal_planner.exception.UserNotFoundException;
import com.lubomirgeorgiev.meal_planner.mapper.user.UserMapper;
import com.lubomirgeorgiev.meal_planner.model.dto.user.ProfileUpdateDto;
import com.lubomirgeorgiev.meal_planner.model.dto.user.UserDto;
import com.lubomirgeorgiev.meal_planner.model.dto.user.UserRegisterRequest;
import com.lubomirgeorgiev.meal_planner.model.entity.group.Group;
import com.lubomirgeorgiev.meal_planner.model.entity.user.GroupChoice;
import com.lubomirgeorgiev.meal_planner.model.entity.user.User;
import com.lubomirgeorgiev.meal_planner.model.entity.user.UserRole;
import com.lubomirgeorgiev.meal_planner.repository.user.UserRepository;
import com.lubomirgeorgiev.meal_planner.service.group.GroupService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private GroupService groupService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, GroupService groupService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.groupService = groupService;
    }

    @Transactional
    public UserDto register (UserRegisterRequest registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail()) || userRepository.existsByUsername(registerDto.getUsername())) {
            throw new UserAlreadyExistsException();
        }

        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new PasswordDoesNotMatchException();
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

    @Transactional(readOnly = true)
    public UserDto getById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        return AuthenticationUserDetails.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public UserDto updateProfile(UUID userId, ProfileUpdateDto profileUpdateDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        user.setUsername(profileUpdateDto.getUsername());
        userRepository.save(user);

        return UserMapper.toUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAllOrderByUsernameAsc();

        return users.stream().map(UserMapper::toUserDto).toList();
    }

    public void changeRole(UUID id, UserRole userRole) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setRole(userRole);

        userRepository.save(user);
    }
}
