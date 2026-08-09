package com.lubomirgeorgiev.meal_planner.service.group;

import com.lubomirgeorgiev.meal_planner.exception.GroupIsAlreadyUpgradedException;
import com.lubomirgeorgiev.meal_planner.exception.GroupNameTakenException;
import com.lubomirgeorgiev.meal_planner.exception.GroupNotFoundException;
import com.lubomirgeorgiev.meal_planner.exception.InvalidGroupPasswordException;
import com.lubomirgeorgiev.meal_planner.mapper.group.GroupMapper;
import com.lubomirgeorgiev.meal_planner.model.dto.group.GroupUpgradeDto;
import com.lubomirgeorgiev.meal_planner.model.entity.group.Group;
import com.lubomirgeorgiev.meal_planner.repository.group.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class GroupService {

    private GroupRepository groupRepository;
    private PasswordEncoder passwordEncoder;

    public GroupService(GroupRepository groupRepository, PasswordEncoder passwordEncoder) {
        this.groupRepository = groupRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Group createGroup(String name, String rawPassword) {
        if (groupRepository.existsByName(name)) {
            throw new GroupNameTakenException("Group already exists");
        }

        String password = rawPassword == null || rawPassword.isBlank() ? null : passwordEncoder.encode(rawPassword);
        Group group = Group.builder()
                .name(name)
                .isDummy(false)
                .isPublic(password == null )
                .password(password)
                .build();
        groupRepository.save(group);
        return group;
    }

    public Group joinGroup (String name, String rawPassword) {
        Group group = groupRepository.findByName(name)
                .orElseThrow(GroupNotFoundException::new);

        if (group.isDummy()) {
            throw new GroupNotFoundException();
        }

        if (!group.isPublic()) {
            if (rawPassword == null || !passwordEncoder.matches(rawPassword, group.getPassword()) ) {
                throw new InvalidGroupPasswordException();
            }
        }

        return group;
    }

    public Group createDummyGroup() {
        Group group = Group.builder()
                .name(null)
                .password(null)
                .isDummy(true)
                .isPublic(false)
                .build();
        return groupRepository.save(group);
    }

    public GroupUpgradeResult upgradeDummyGroup(UUID groupId, GroupUpgradeDto groupUpgradeDto, UUID userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(GroupNotFoundException::new);

        if (!group.isDummy()) {
            throw new GroupIsAlreadyUpgradedException();
        }

        if (groupRepository.existsByName(groupUpgradeDto.getName())) {
            throw new GroupNameTakenException("Group already exists");
        }

        group.setName(groupUpgradeDto.getName());
        group.setDummy(false);
        group.setPublic(groupUpgradeDto.isPublicGroup());

        String rawCode = null;
        if (!groupUpgradeDto.isPublicGroup() && groupUpgradeDto.isGeneratePassword()) {
            rawCode = generateEightDigitCode();
            group.setPassword(passwordEncoder.encode(rawCode));
        } else {
            group.setPassword(null);
        }

        Group savedGroup = groupRepository.save(group);

        log.info("User {} upgraded group {} to named group", userId, group.getId());

        return new GroupUpgradeResult(GroupMapper.toGroupDto(savedGroup), rawCode);
    }

    private String generateEightDigitCode() {
        return String.format("%08d", new Random().nextInt(100_000_000));
    }
}
