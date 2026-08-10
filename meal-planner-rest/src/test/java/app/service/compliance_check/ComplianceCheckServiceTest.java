package app.service.compliance_check;

import app.model.dto.compliance_check.ComplianceCheckRequest;
import app.model.dto.compliance_check.ComplianceCheckResponse;
import app.model.dto.compliance_check.WeeklyComplianceSummaryResponse;
import app.model.entity.compliance_check.ComplianceCheck;
import app.repository.compliance_check.ComplianceCheckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComplianceCheckServiceTest {

    @Mock
    private ComplianceCheckRepository complianceCheckRepository;

    private ComplianceCheckService complianceCheckService;

    @BeforeEach
    void setUp() {
        complianceCheckService = new ComplianceCheckService(complianceCheckRepository);
    }

    @Test
    void record_whenUnderTarget_savesWithinTargetTrue() {
        UUID userId = UUID.randomUUID();
        ComplianceCheckRequest request = ComplianceCheckRequest.builder()
                .externalUserId(userId)
                .checkDate(LocalDate.now())
                .totalCaloriesConsumed(1800)
                .targetedCalories(2000)
                .build();

        ComplianceCheckResponse response = complianceCheckService.record(request);

        assertThat(response.isWithinTarget()).isTrue();

        ArgumentCaptor<ComplianceCheck> captor = ArgumentCaptor.forClass(ComplianceCheck.class);
        verify(complianceCheckRepository).save(captor.capture());
        assertThat(captor.getValue().isWithinTarget()).isTrue();
    }

    @Test
    void record_whenOverTarget_savesWithinTargetFalse() {
        UUID userId = UUID.randomUUID();
        ComplianceCheckRequest request = ComplianceCheckRequest.builder()
                .externalUserId(userId)
                .checkDate(LocalDate.now())
                .totalCaloriesConsumed(2500)
                .targetedCalories(2000)
                .build();

        ComplianceCheckResponse response = complianceCheckService.record(request);

        assertThat(response.isWithinTarget()).isFalse();
    }

    @Test
    void record_whenExactlyAtTarget_isWithinTarget() {
        UUID userId = UUID.randomUUID();
        ComplianceCheckRequest request = ComplianceCheckRequest.builder()
                .externalUserId(userId)
                .checkDate(LocalDate.now())
                .totalCaloriesConsumed(2000)
                .targetedCalories(2000)
                .build();

        ComplianceCheckResponse response = complianceCheckService.record(request);

        // service uses <=, so hitting the target exactly still counts as compliant
        assertThat(response.isWithinTarget()).isTrue();
    }

    @Test
    void getWeeklySummary_aggregatesWithinCountAndChecks() {
        UUID userId = UUID.randomUUID();
        LocalDate today = LocalDate.now();

        ComplianceCheck within = ComplianceCheck.builder()
                .id(UUID.randomUUID())
                .externalUserId(userId)
                .checkDate(today)
                .totalCaloriesConsumed(1800)
                .targetCalories(2000)
                .withinTarget(true)
                .build();

        ComplianceCheck over = ComplianceCheck.builder()
                .id(UUID.randomUUID())
                .externalUserId(userId)
                .checkDate(today.minusDays(1))
                .totalCaloriesConsumed(2500)
                .targetCalories(2000)
                .withinTarget(false)
                .build();

        when(complianceCheckRepository.findByExternalUserIdAndCheckDateBetween(eq(userId), any(), any()))
                .thenReturn(List.of(within, over));

        WeeklyComplianceSummaryResponse response = complianceCheckService.getWeeklySummary(userId);

        assertThat(response.getExternalUserId()).isEqualTo(userId);
        assertThat(response.getDaysWithinTarget()).isEqualTo(1);
        assertThat(response.getChecks()).hasSize(2);

        // NOTE: ComplianceCheckMapper#toWeeklyComplianceSummaryResponse currently sets
        // daysOverTarget to complianceCheckList.size() rather than the daysOverTarget
        // value the service computed and passed in (which would be 1 here). This
        // assertion documents the actual current behavior — see ComplianceCheckMapperTest
        // for the same finding isolated at the mapper level.
        assertThat(response.getDaysOverTarget()).isEqualTo(2);
    }

    @Test
    void getWeeklySummary_whenNoChecks_returnsZeroedSummary() {
        UUID userId = UUID.randomUUID();

        when(complianceCheckRepository.findByExternalUserIdAndCheckDateBetween(eq(userId), any(), any()))
                .thenReturn(List.of());

        WeeklyComplianceSummaryResponse response = complianceCheckService.getWeeklySummary(userId);

        assertThat(response.getDaysWithinTarget()).isEqualTo(0);
        assertThat(response.getDaysOverTarget()).isEqualTo(0);
        assertThat(response.getChecks()).isEmpty();
    }
}
