package com.lubomirgeorgiev.meal_planner.model.dto.group;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class GroupDto {

    private UUID id;
    private String name;
    private boolean dummy;
    private boolean publicGroup;
    private boolean hasPassword;
}
