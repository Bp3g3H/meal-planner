package app.model.entity.compliance_check;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compliance_check")
public class ComplianceCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID externalUserId;

    @Column(nullable = false)
    private LocalDate checkDate;

    @Column(nullable = false)
    private Integer totalCaloriesConsumed;

    @Column(nullable = false)
    private Integer targetCalories;

    @Column(nullable = false)
    private boolean withinTarget;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;
}
