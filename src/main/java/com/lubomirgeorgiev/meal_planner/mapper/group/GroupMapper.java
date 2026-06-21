package com.lubomirgeorgiev.meal_planner.mapper.group;

import com.lubomirgeorgiev.meal_planner.model.dto.group.GroupDto;
import com.lubomirgeorgiev.meal_planner.model.entity.group.Group;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GroupMapper {

    public static GroupDto toGroupDto(Group group) {
        if (group == null) {
            return null;
        }

        return GroupDto.builder()
                .id(group.getId())
                .name(group.getName())
                .dummy(group.isDummy())
                .publicGroup(group.isPublic())
                .hasPassword(group.hasPassword())
                .build();
    }
}
