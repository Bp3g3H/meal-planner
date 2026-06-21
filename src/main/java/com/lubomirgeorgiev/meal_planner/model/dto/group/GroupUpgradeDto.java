package com.lubomirgeorgiev.meal_planner.model.dto.group;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupUpgradeDto {

    private String name;
    boolean publicGroup;
    boolean generatePassword;
}
