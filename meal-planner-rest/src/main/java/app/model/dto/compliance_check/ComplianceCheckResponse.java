package app.model.dto.compliance_check;

import java.time.LocalDate;
import java.util.UUID;

public class ComplianceCheckResponse {

    private UUID id;
    private LocalDate checkDate;
    private Integer totalCaloriesConsumed;
    private Integer targetCalories;
    private boolean withinTarget;
}
