package app.mapper.compliance_check;

import app.model.dto.compliance_check.ComplianceCheckResponse;
import app.model.dto.compliance_check.WeeklyComplianceSummaryResponse;
import app.model.entity.compliance_check.ComplianceCheck;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
public class ComplianceCheckMapper {

    public static ComplianceCheckResponse toComplianceCheckResponse(ComplianceCheck complianceCheck) {
        return ComplianceCheckResponse.builder()
                .id(complianceCheck.getId())
                .checkDate(complianceCheck.getCheckDate())
                .totalCaloriesConsumed(complianceCheck.getTotalCaloriesConsumed())
                .targetCalories(complianceCheck.getTargetCalories())
                .withinTarget(complianceCheck.isWithinTarget())
                .build();
    }

    public static WeeklyComplianceSummaryResponse toWeeklyComplianceSummaryResponse(
            UUID externalUserId, int daysWithingTarget, int daysOverTarget, List<ComplianceCheck> complianceCheckList) {

        List<ComplianceCheckResponse> complianceCheckResponseList = new ArrayList<>();

        for (ComplianceCheck complianceCheck : complianceCheckList) {
            complianceCheckResponseList.add(toComplianceCheckResponse(complianceCheck));
        }

        return WeeklyComplianceSummaryResponse.builder()
                .externalUserId(externalUserId)
                .daysWithinTarget(daysWithingTarget)
                .daysOverTarget(complianceCheckList.size())
                .checks(complianceCheckResponseList)
                .build();
    }
}
