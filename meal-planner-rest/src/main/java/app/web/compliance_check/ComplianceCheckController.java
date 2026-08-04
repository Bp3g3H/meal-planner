package app.web.compliance_check;

import app.model.dto.compliance_check.ComplianceCheckRequest;
import app.model.dto.compliance_check.ComplianceCheckResponse;
import app.model.dto.compliance_check.WeeklyComplianceSummaryResponse;
import app.service.compliance_check.ComplianceCheckService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/compliance-checks")
public class ComplianceCheckController {

    private final ComplianceCheckService complianceCheckService;

    public ComplianceCheckController(ComplianceCheckService complianceCheckService) {
        this.complianceCheckService = complianceCheckService;
    }

    @PostMapping
    public ResponseEntity<ComplianceCheckResponse> recordComplianceCheck(@Valid @RequestBody ComplianceCheckRequest complianceCheckRequest) {
        ComplianceCheckResponse response = complianceCheckService.record(complianceCheckRequest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/weekly")
    public ResponseEntity<WeeklyComplianceSummaryResponse> getWeeklyComplianceSummary(@PathVariable UUID userId) {
        WeeklyComplianceSummaryResponse summaryResponse = complianceCheckService.getWeeklySummary(userId);

        return ResponseEntity.ok(summaryResponse);
    }

}
