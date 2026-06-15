package com.lubomirgeorgiev.meal_planner.service.group;

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
            // TODO change to GroupNameTakenException
            throw new RuntimeException("Group already exists");
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
                // TODO change to GroupNotFoundException
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (group.isDummy()) {
            // TODO change to GroupNotFoundException
            new RuntimeException("Group not found");
        }

        if (!group.isPublic()) {
            if (rawPassword == null || !passwordEncoder.matches(rawPassword, group.getPassword()) ) {
                // TODO change to InvalidGroupPasswordException
                throw new RuntimeException("Passwords do not match");
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
                // TODO GroupNotFoundException
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.isDummy()) {
            throw new IllegalStateException("(Group is already upgraded");
        }

        if (groupRepository.existsByName(group.getName())) {
            // TODO GroupNameTakenException
            throw new RuntimeException("Group already exists");
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
