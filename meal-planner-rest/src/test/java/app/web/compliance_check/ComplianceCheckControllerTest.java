package app.web.compliance_check;

import app.model.dto.compliance_check.ComplianceCheckRequest;
import app.model.dto.compliance_check.ComplianceCheckResponse;
import app.model.dto.compliance_check.WeeklyComplianceSummaryResponse;
import app.service.compliance_check.ComplianceCheckService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComplianceCheckController.class)
class ComplianceCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ComplianceCheckService complianceCheckService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void recordComplianceCheck_withValidBody_returns200() throws Exception {
        UUID userId = UUID.randomUUID();
        ComplianceCheckRequest request = ComplianceCheckRequest.builder()
                .externalUserId(userId)
                .checkDate(LocalDate.now())
                .totalCaloriesConsumed(1900)
                .targetedCalories(2000)
                .build();
        ComplianceCheckResponse response = ComplianceCheckResponse.builder()
                .id(UUID.randomUUID())
                .checkDate(LocalDate.now())
                .totalCaloriesConsumed(1900)
                .targetCalories(2000)
                .withinTarget(true)
                .build();

        when(complianceCheckService.record(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/compliance-checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.withinTarget").value(true));
    }

    @Test
    void recordComplianceCheck_withMissingFields_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/compliance-checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void recordComplianceCheck_withNegativeCalories_returns400() throws Exception {
        String invalidJson = """
                {
                  "externalUserId": "%s",
                  "checkDate": "2026-01-01",
                  "totalCaloriesConsumed": -50,
                  "targetedCalories": 2000
                }
                """.formatted(UUID.randomUUID());

        mockMvc.perform(post("/api/v1/compliance-checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getWeeklyComplianceSummary_returns200WithSummary() throws Exception {
        UUID userId = UUID.randomUUID();
        WeeklyComplianceSummaryResponse response = WeeklyComplianceSummaryResponse.builder()
                .externalUserId(userId)
                .daysWithinTarget(5)
                .daysOverTarget(2)
                .checks(List.of())
                .build();

        when(complianceCheckService.getWeeklySummary(eq(userId))).thenReturn(response);

        mockMvc.perform(get("/api/v1/compliance-checks/{userId}/weekly", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.daysWithinTarget").value(5))
                .andExpect(jsonPath("$.externalUserId").value(userId.toString()));
    }
}
