package app.model.entity.nutrition_goal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nutrition_goal")
public class NutritionGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID externalUserId;

    @Column(nullable = false)
    private int dailyCalorieTarget;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedOn;

}
