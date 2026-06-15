package com.lubomirgeorgiev.meal_planner.repository.group;

import com.lubomirgeorgiev.meal_planner.model.entity.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {
    Optional<Group> findByName(String name);
    boolean existsByName(String name);
}
