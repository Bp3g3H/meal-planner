package app.service.nutrition_goal;

import app.exception.NutritionGoalAlreadyExistsException;
import app.exception.NutritionGoalNotFoundException;
import app.model.dto.nutrition_goal.NutritionGoalRequest;
import app.model.dto.nutrition_goal.NutritionGoalResponse;
import app.model.entity.nutrition_goal.NutritionGoal;
import app.repository.nutrition_goal.NutritionGoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NutritionGoalServiceTest {

    @Mock
    private NutritionGoalRepository nutritionGoalRepository;

    private NutritionGoalService nutritionGoalService;

    @BeforeEach
    void setUp() {
        nutritionGoalService = new NutritionGoalService(nutritionGoalRepository);
    }

    @Test
    void create_whenGoalDoesNotExist_savesAndReturnsResponse() {
        UUID userId = UUID.randomUUID();
        NutritionGoalRequest request = NutritionGoalRequest.builder()
                .externalUserId(userId)
                .dailyCalorieTarget(2000)
                .build();

        when(nutritionGoalRepository.existsByExternalUserId(userId)).thenReturn(false);

        NutritionGoalResponse response = nutritionGoalService.create(request);

        assertThat(response.getExternalUserId()).isEqualTo(userId);
        assertThat(response.getDailyCalorieTarget()).isEqualTo(2000);

        ArgumentCaptor<NutritionGoal> captor = ArgumentCaptor.forClass(NutritionGoal.class);
        verify(nutritionGoalRepository).save(captor.capture());
        assertThat(captor.getValue().getExternalUserId()).isEqualTo(userId);
        assertThat(captor.getValue().getDailyCalorieTarget()).isEqualTo(2000);
    }

    @Test
    void create_whenGoalAlreadyExists_throws() {
        UUID userId = UUID.randomUUID();
        NutritionGoalRequest request = NutritionGoalRequest.builder()
                .externalUserId(userId)
                .dailyCalorieTarget(2000)
                .build();

        when(nutritionGoalRepository.existsByExternalUserId(userId)).thenReturn(true);

        assertThatThrownBy(() -> nutritionGoalService.create(request))
                .isInstanceOf(NutritionGoalAlreadyExistsException.class)
                .hasMessageContaining(userId.toString());

        verify(nutritionGoalRepository, never()).save(any());
    }

    @Test
    void update_whenGoalExists_updatesAndReturnsResponse() {
        UUID userId = UUID.randomUUID();
        NutritionGoal existing = NutritionGoal.builder()
                .id(UUID.randomUUID())
                .externalUserId(userId)
                .dailyCalorieTarget(1800)
                .createdOn(LocalDateTime.now().minusDays(1))
                .updatedOn(LocalDateTime.now().minusDays(1))
                .build();

        NutritionGoalRequest request = NutritionGoalRequest.builder()
                .externalUserId(userId)
                .dailyCalorieTarget(2200)
                .build();

        when(nutritionGoalRepository.findByExternalUserId(userId)).thenReturn(Optional.of(existing));

        NutritionGoalResponse response = nutritionGoalService.update(request);

        assertThat(response.getDailyCalorieTarget()).isEqualTo(2200);
        verify(nutritionGoalRepository).save(existing);
    }

    @Test
    void update_whenGoalDoesNotExist_throws() {
        UUID userId = UUID.randomUUID();
        NutritionGoalRequest request = NutritionGoalRequest.builder()
                .externalUserId(userId)
                .dailyCalorieTarget(2200)
                .build();

        when(nutritionGoalRepository.findByExternalUserId(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> nutritionGoalService.update(request))
                .isInstanceOf(NutritionGoalNotFoundException.class)
                .hasMessageContaining(userId.toString());
    }

    @Test
    void findByExternalUserId_whenFound_returnsResponse() {
        UUID userId = UUID.randomUUID();
        NutritionGoal goal = NutritionGoal.builder()
                .id(UUID.randomUUID())
                .externalUserId(userId)
                .dailyCalorieTarget(2100)
                .updatedOn(LocalDateTime.now())
                .build();

        when(nutritionGoalRepository.findByExternalUserId(userId)).thenReturn(Optional.of(goal));

        NutritionGoalResponse response = nutritionGoalService.findByExternalUserId(userId);

        assertThat(response).isNotNull();
        assertThat(response.getDailyCalorieTarget()).isEqualTo(2100);
    }

    @Test
    void findByExternalUserId_whenNotFound_returnsNull() {
        UUID userId = UUID.randomUUID();
        when(nutritionGoalRepository.findByExternalUserId(userId)).thenReturn(Optional.empty());

        NutritionGoalResponse response = nutritionGoalService.findByExternalUserId(userId);

        assertThat(response).isNull();
    }
}
