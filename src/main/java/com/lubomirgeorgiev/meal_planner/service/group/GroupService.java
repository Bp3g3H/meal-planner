package com.lubomirgeorgiev.meal_planner.service.group;

import com.lubomirgeorgiev.meal_planner.exception.GroupNameTakenException;
import com.lubomirgeorgiev.meal_planner.exception.GroupNotFoundException;
import com.lubomirgeorgiev.meal_planner.exception.InvalidGroupPasswordException;
import com.lubomirgeorgiev.meal_planner.model.dto.group.GroupUpgradeDto;
import com.lubomirgeorgiev.meal_planner.model.entity.group.Group;
import com.lubomirgeorgiev.meal_planner.repository.group.GroupRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class GroupService {

    private GroupRepository groupRepository;
    private PasswordEncoder passwordEncoder;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
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
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));

        if (group.isDummy()) {
            new GroupNotFoundException("Group not found");
        }

        if (!group.isPublic()) {
            if (rawPassword == null || !passwordEncoder.matches(rawPassword, group.getPassword()) ) {
                throw new InvalidGroupPasswordException();
            }
        }

        return group;
    }

    public Group createDummyGroup() {
        return Group.builder()
                .name(null)
                .password(null)
                .isDummy(true)
                .isPublic(false)
                .build();
    }

    public Group upgradeDummyGroup(UUID groupId, GroupUpgradeDto groupUpgradeDto, UUID userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));

        if (!group.isDummy()) {
            throw new IllegalStateException("(Group is already upgraded");
        }

        if (groupRepository.existsByName(group.getName())) {
            throw new GroupNameTakenException("Group already exists");
        }

        group.setName(groupUpgradeDto.getName());
        group.setDummy(false);
        group.setPublic(groupUpgradeDto.isPublic());

        if (!groupUpgradeDto.isPublic() && groupUpgradeDto.isGeneratePassword()) {
            String rawCode = generateEightDigitCode();
            group.setPassword(passwordEncoder.encode(rawCode));
        }

        return groupRepository.save(group);
    }

    private String generateEightDigitCode() {
        return String.format("%08d", new Random().nextInt(100_000_000));
    }
}
