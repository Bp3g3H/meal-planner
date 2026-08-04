package app.model.dto.compliance_check;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class WeeklyComplianceSummaryResponse {

    private UUID externalUserId;
    private Integer daysWithinTarget;
    private Integer daysOverTarget;
    private List<ComplianceCheckResponse> checks;
}
