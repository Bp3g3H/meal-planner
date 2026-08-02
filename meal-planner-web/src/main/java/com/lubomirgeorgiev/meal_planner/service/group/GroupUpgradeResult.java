package com.lubomirgeorgiev.meal_planner.service.group;

import com.lubomirgeorgiev.meal_planner.model.dto.group.GroupDto;

public record GroupUpgradeResult(GroupDto groupDto, String rawCode) {
}