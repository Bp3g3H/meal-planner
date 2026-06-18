package com.lubomirgeorgiev.meal_planner.model.entity.group;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`group`")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String name;
    private String password;
    @Column(columnDefinition = "boolean default true")
    private boolean isDummy;
    @Column(columnDefinition = "boolean default false")
    private boolean isPublic;
    @CreationTimestamp
    private LocalDateTime createdOn;
}
