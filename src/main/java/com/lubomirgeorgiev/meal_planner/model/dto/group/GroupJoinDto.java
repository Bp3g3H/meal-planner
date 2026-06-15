package com.lubomirgeorgiev.meal_planner.model.dto.group;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupJoinDto {

    private String groupName;
    private String groupPassword;
}
