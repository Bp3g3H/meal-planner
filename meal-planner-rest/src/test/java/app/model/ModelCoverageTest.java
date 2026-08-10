package app.model;

import app.model.dto.compliance_check.ComplianceCheckRequest;
import app.model.dto.compliance_check.ComplianceCheckResponse;
import app.model.dto.compliance_check.WeeklyComplianceSummaryResponse;
import app.model.dto.error.ErrorResponse;
import app.model.dto.nutrition_goal.NutritionGoalRequest;
import app.model.dto.nutrition_goal.NutritionGoalResponse;
import app.model.entity.compliance_check.ComplianceCheck;
import app.model.entity.nutrition_goal.NutritionGoal;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * These don't test business behavior — they exist to exercise the
 * Lombok-generated equals()/hashCode()/toString()/constructors on the
 * entity and DTO classes. None of the other tests happen to invoke those
 * directly (they compare individual fields, not whole objects), and
 * leaving them completely uncovered noticeably drags down the JaCoCo line
 * count given how many classes here are @Data/@Builder.
 */
class ModelCoverageTest {

    @Test
    void nutritionGoal_equalsHashCodeToStringAndNoArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        NutritionGoal a = new NutritionGoal(id, userId, 2000, now, now);
        NutritionGoal b = NutritionGoal.builder()
                .id(id).externalUserId(userId).dailyCalorieTarget(2000).createdOn(now).updatedOn(now).build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a.toString()).contains("NutritionGoal");

        NutritionGoal viaSetters = new NutritionGoal();
        viaSetters.setDailyCalorieTarget(1500);
        assertThat(viaSetters.getDailyCalorieTarget()).isEqualTo(1500);
    }

    @Test
    void complianceCheck_equalsHashCodeToStringAndNoArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        ComplianceCheck a = new ComplianceCheck(id, userId, today, 1800, 2000, true, now);
        ComplianceCheck b = ComplianceCheck.builder()
                .id(id).externalUserId(userId).checkDate(today)
                .totalCaloriesConsumed(1800).targetCalories(2000).withinTarget(true).createdOn(now).build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a.toString()).contains("ComplianceCheck");

        ComplianceCheck viaSetters = new ComplianceCheck();
        viaSetters.setTotalCaloriesConsumed(1200);
        assertThat(viaSetters.getTotalCaloriesConsumed()).isEqualTo(1200);
    }

    @Test
    void nutritionGoalRequest_equalsHashCodeToString() {
        UUID userId = UUID.randomUUID();
        NutritionGoalRequest a = NutritionGoalRequest.builder().externalUserId(userId).dailyCalorieTarget(2000).build();
        NutritionGoalRequest b = NutritionGoalRequest.builder().externalUserId(userId).dailyCalorieTarget(2000).build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a.toString()).contains("NutritionGoalRequest");
    }

    @Test
    void nutritionGoalResponse_equalsHashCodeToString() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime updatedOn = LocalDateTime.now();

        NutritionGoalResponse a = NutritionGoalResponse.builder()
                .id(id).externalUserId(userId).dailyCalorieTarget(2000).updatedOn(updatedOn).build();
        NutritionGoalResponse b = NutritionGoalResponse.builder()
                .id(id).externalUserId(userId).dailyCalorieTarget(2000).updatedOn(updatedOn).build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a.toString()).contains("NutritionGoalResponse");
    }

    @Test
    void complianceCheckRequest_equalsHashCodeToString() {
        UUID userId = UUID.randomUUID();
        LocalDate date = LocalDate.now();

        ComplianceCheckRequest a = ComplianceCheckRequest.builder()
                .externalUserId(userId).checkDate(date).totalCaloriesConsumed(1800).targetedCalories(2000).build();
        ComplianceCheckRequest b = ComplianceCheckRequest.builder()
                .externalUserId(userId).checkDate(date).totalCaloriesConsumed(1800).targetedCalories(2000).build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a.toString()).contains("ComplianceCheckRequest");
    }

    @Test
    void complianceCheckResponse_equalsHashCodeToString() {
        UUID id = UUID.randomUUID();
        LocalDate date = LocalDate.now();

        ComplianceCheckResponse a = ComplianceCheckResponse.builder()
                .id(id).checkDate(date).totalCaloriesConsumed(1800).targetCalories(2000).withinTarget(true).build();
        ComplianceCheckResponse b = ComplianceCheckResponse.builder()
                .id(id).checkDate(date).totalCaloriesConsumed(1800).targetCalories(2000).withinTarget(true).build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a.toString()).contains("ComplianceCheckResponse");
    }

    @Test
    void weeklyComplianceSummaryResponse_equalsHashCodeToString() {
        UUID userId = UUID.randomUUID();

        WeeklyComplianceSummaryResponse a = WeeklyComplianceSummaryResponse.builder()
                .externalUserId(userId).daysWithinTarget(3).daysOverTarget(4).checks(List.of()).build();
        WeeklyComplianceSummaryResponse b = WeeklyComplianceSummaryResponse.builder()
                .externalUserId(userId).daysWithinTarget(3).daysOverTarget(4).checks(List.of()).build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a.toString()).contains("WeeklyComplianceSummaryResponse");
    }

    @Test
    void errorResponse_equalsHashCodeToStringAndNoArgsConstructor() {
        LocalDateTime timestamp = LocalDateTime.now();

        ErrorResponse a = ErrorResponse.builder()
                .timestamp(timestamp).status(404).error("Not Found")
                .message("test").path("/api/v1/test").build();
        ErrorResponse b = new ErrorResponse(timestamp, 404, "Not Found", "test", "/api/v1/test");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a.toString()).contains("ErrorResponse");

        ErrorResponse viaSetters = new ErrorResponse();
        viaSetters.setStatus(500);
        assertThat(viaSetters.getStatus()).isEqualTo(500);
    }
}
