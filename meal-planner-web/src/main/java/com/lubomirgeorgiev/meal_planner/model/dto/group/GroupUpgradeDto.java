package com.lubomirgeorgiev.meal_planner.model.dto.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupUpgradeDto {

    @NotBlank
    @Size(max = 30)
    private String name;
    boolean publicGroup;
    boolean generatePassword;
}
