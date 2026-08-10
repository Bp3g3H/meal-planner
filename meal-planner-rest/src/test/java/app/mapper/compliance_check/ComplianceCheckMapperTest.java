package app.mapper.compliance_check;

import app.model.dto.compliance_check.ComplianceCheckResponse;
import app.model.dto.compliance_check.WeeklyComplianceSummaryResponse;
import app.model.entity.compliance_check.ComplianceCheck;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ComplianceCheckMapperTest {

    @Test
    void toComplianceCheckResponse_mapsAllFields() {
        UUID id = UUID.randomUUID();
        LocalDate checkDate = LocalDate.now();

        ComplianceCheck check = ComplianceCheck.builder()
                .id(id)
                .externalUserId(UUID.randomUUID())
                .checkDate(checkDate)
                .totalCaloriesConsumed(1900)
                .targetCalories(2000)
                .withinTarget(true)
                .build();

        ComplianceCheckResponse response = ComplianceCheckMapper.toComplianceCheckResponse(check);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getCheckDate()).isEqualTo(checkDate);
        assertThat(response.getTotalCaloriesConsumed()).isEqualTo(1900);
        assertThat(response.getTargetCalories()).isEqualTo(2000);
        assertThat(response.isWithinTarget()).isTrue();
    }

    @Test
    void toWeeklyComplianceSummaryResponse_mapsChecksAndWithinCount() {
        UUID userId = UUID.randomUUID();
        LocalDate today = LocalDate.now();

        ComplianceCheck check1 = ComplianceCheck.builder()
                .id(UUID.randomUUID()).externalUserId(userId).checkDate(today)
                .totalCaloriesConsumed(1800).targetCalories(2000).withinTarget(true).build();
        ComplianceCheck check2 = ComplianceCheck.builder()
                .id(UUID.randomUUID()).externalUserId(userId).checkDate(today.minusDays(1))
                .totalCaloriesConsumed(2400).targetCalories(2000).withinTarget(false).build();

        WeeklyComplianceSummaryResponse response = ComplianceCheckMapper.toWeeklyComplianceSummaryResponse(
                userId, 1, 1, List.of(check1, check2));

        assertThat(response.getExternalUserId()).isEqualTo(userId);
        assertThat(response.getDaysWithinTarget()).isEqualTo(1);
        assertThat(response.getChecks()).hasSize(2);
    }

    @Test
    void toWeeklyComplianceSummaryResponse_daysOverTargetCurrentlyIgnoresItsArgument() {
        // NOTE (bug documentation): the daysOverTarget parameter passed in here is 1,
        // but the mapper implementation sets .daysOverTarget(complianceCheckList.size())
        // instead of using the parameter — so the response ends up with the total
        // check count (2), not the actual over-target count (1). This test pins down
        // that current behavior so a future fix to the mapper shows up here as a
        // clear, intentional test failure rather than a silent behavior change.
        UUID userId = UUID.randomUUID();
        LocalDate today = LocalDate.now();

        ComplianceCheck check1 = ComplianceCheck.builder()
                .id(UUID.randomUUID()).externalUserId(userId).checkDate(today)
                .totalCaloriesConsumed(1800).targetCalories(2000).withinTarget(true).build();
        ComplianceCheck check2 = ComplianceCheck.builder()
                .id(UUID.randomUUID()).externalUserId(userId).checkDate(today.minusDays(1))
                .totalCaloriesConsumed(2400).targetCalories(2000).withinTarget(false).build();

        WeeklyComplianceSummaryResponse response = ComplianceCheckMapper.toWeeklyComplianceSummaryResponse(
                userId, 1, 1, List.of(check1, check2));

        assertThat(response.getDaysOverTarget()).isEqualTo(2);
    }

    @Test
    void toWeeklyComplianceSummaryResponse_withEmptyList_returnsEmptySummary() {
        UUID userId = UUID.randomUUID();

        WeeklyComplianceSummaryResponse response = ComplianceCheckMapper.toWeeklyComplianceSummaryResponse(
                userId, 0, 0, List.of());

        assertThat(response.getChecks()).isEmpty();
        assertThat(response.getDaysWithinTarget()).isEqualTo(0);
        assertThat(response.getDaysOverTarget()).isEqualTo(0);
    }
}
