package app.service.compliance_check;

import app.mapper.compliance_check.ComplianceCheckMapper;
import app.model.dto.compliance_check.ComplianceCheckRequest;
import app.model.dto.compliance_check.ComplianceCheckResponse;
import app.model.dto.compliance_check.WeeklyComplianceSummaryResponse;
import app.model.entity.compliance_check.ComplianceCheck;
import app.repository.compliance_check.ComplianceCheckRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ComplianceCheckService {

    private ComplianceCheckRepository complianceCheckRepository;

    public ComplianceCheckService(ComplianceCheckRepository complianceCheckRepository) {
        this.complianceCheckRepository = complianceCheckRepository;
    }

    public ComplianceCheckResponse record(ComplianceCheckRequest complianceCheckRequest) {
        ComplianceCheck complianceCheck = ComplianceCheck.builder()
                .externalUserId(complianceCheckRequest.getExternalUserId())
                .checkDate(complianceCheckRequest.getCheckDate())
                .totalCaloriesConsumed(complianceCheckRequest.getTotalCaloriesConsumed())
                .targetCalories(complianceCheckRequest.getTargetedCalories())
                .withinTarget(complianceCheckRequest.getTotalCaloriesConsumed() <=  complianceCheckRequest.getTargetedCalories())
                .createdOn(LocalDateTime.now())
                .build();

        complianceCheckRepository.save(complianceCheck);

        return ComplianceCheckMapper.toComplianceCheckResponse(complianceCheck);
    }

    public WeeklyComplianceSummaryResponse getWeeklySummary(UUID externalUserId) {
        LocalDate today =  LocalDate.now();
        List<ComplianceCheck> complianceCheckList = complianceCheckRepository.findByExternalUserIdAndCheckDateBetween(externalUserId, today.minusDays(6), today);

        int daysWithingTarget = (int) complianceCheckList.stream().filter(ComplianceCheck::isWithinTarget).count();
        int daysOverTarget = complianceCheckList.size() - daysWithingTarget;

        return ComplianceCheckMapper.toWeeklyComplianceSummaryResponse(externalUserId, daysWithingTarget, daysOverTarget, complianceCheckList);
    }
}
