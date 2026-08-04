package app.exception;

import app.model.dto.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class NutritionApiExceptionHandler {

    @ExceptionHandler(NutritionGoalAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleNutritionGoalAlreadyExistingException(HttpServletRequest request, NutritionGoalAlreadyExistsException exception) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NutritionGoalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNutritionGoalNotFoundException(HttpServletRequest request, NutritionGoalNotFoundException exception) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}
