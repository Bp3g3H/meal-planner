package app.web.nutrition_goal;

import app.model.dto.nutrition_goal.NutritionGoalRequest;
import app.model.dto.nutrition_goal.NutritionGoalResponse;
import app.service.nutrition_goal.NutritionGoalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NutritionGoalController.class)
class NutritionGoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NutritionGoalService nutritionGoalService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_withValidBody_returns200() throws Exception {
        UUID userId = UUID.randomUUID();
        NutritionGoalRequest request = NutritionGoalRequest.builder()
                .externalUserId(userId)
                .dailyCalorieTarget(2000)
                .build();
        NutritionGoalResponse response = NutritionGoalResponse.builder()
                .id(UUID.randomUUID())
                .externalUserId(userId)
                .dailyCalorieTarget(2000)
                .updatedOn(LocalDateTime.now())
                .build();

        when(nutritionGoalService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/nutrition-goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.externalUserId").value(userId.toString()))
                .andExpect(jsonPath("$.dailyCalorieTarget").value(2000));
    }

    @Test
    void create_withDailyCalorieTargetBelowMinimum_returns400() throws Exception {
        String invalidJson = """
                {
                  "externalUserId": "%s",
                  "dailyCalorieTarget": 100
                }
                """.formatted(UUID.randomUUID());

        mockMvc.perform(post("/api/v1/nutrition-goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_withMissingExternalUserId_returns400() throws Exception {
        String invalidJson = """
                {
                  "dailyCalorieTarget": 2000
                }
                """;

        mockMvc.perform(post("/api/v1/nutrition-goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    void create_whenGoalAlreadyExists_isHandledByExceptionHandler() throws Exception {
//        UUID userId = UUID.randomUUID();
//        NutritionGoalRequest request = NutritionGoalRequest.builder()
//                .externalUserId(userId)
//                .dailyCalorieTarget(2000)
//                .build();
//
//        when(nutritionGoalService.create(any()))
//                .thenThrow(new NutritionGoalAlreadyExistsException(userId));
//
//        mockMvc.perform(post("/api/v1/nutrition-goals")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isConflict())
//                .andExpect(jsonPath("$.message", containsString(userId.toString())))
//                .andExpect(jsonPath("$.path").value("/api/v1/nutrition-goals"));
//    }

    @Test
    void update_withValidBody_returns200() throws Exception {
        UUID userId = UUID.randomUUID();
        NutritionGoalRequest request = NutritionGoalRequest.builder()
                .externalUserId(userId)
                .dailyCalorieTarget(2500)
                .build();
        NutritionGoalResponse response = NutritionGoalResponse.builder()
                .id(UUID.randomUUID())
                .externalUserId(userId)
                .dailyCalorieTarget(2500)
                .updatedOn(LocalDateTime.now())
                .build();

        when(nutritionGoalService.update(any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/nutrition-goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dailyCalorieTarget").value(2500));
    }

//    @Test
//    void update_whenGoalNotFound_isHandledByExceptionHandler() throws Exception {
//        UUID userId = UUID.randomUUID();
//        NutritionGoalRequest request = NutritionGoalRequest.builder()
//                .externalUserId(userId)
//                .dailyCalorieTarget(2000)
//                .build();
//
//        when(nutritionGoalService.update(any()))
//                .thenThrow(new NutritionGoalNotFoundException(userId));
//
//        mockMvc.perform(put("/api/v1/nutrition-goals")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message", containsString(userId.toString())));
//    }

    @Test
    void findByUserId_returns200WithBody() throws Exception {
        UUID userId = UUID.randomUUID();
        NutritionGoalResponse response = NutritionGoalResponse.builder()
                .id(UUID.randomUUID())
                .externalUserId(userId)
                .dailyCalorieTarget(1900)
                .updatedOn(LocalDateTime.now())
                .build();

        when(nutritionGoalService.findByExternalUserId(eq(userId))).thenReturn(response);

        mockMvc.perform(get("/api/v1/nutrition-goals/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dailyCalorieTarget").value(1900));
    }

    @Test
    void findByUserId_whenServiceReturnsNull_returns200WithEmptyBody() throws Exception {
        UUID userId = UUID.randomUUID();
        when(nutritionGoalService.findByExternalUserId(eq(userId))).thenReturn(null);

        mockMvc.perform(get("/api/v1/nutrition-goals/{userId}", userId))
                .andExpect(status().isOk());
    }
}
