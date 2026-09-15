package com.example.exe101_bioverse.common.exception;

import com.example.exe101_bioverse.exam.dto.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

/**
 * Gom các lỗi validation về đúng định dạng {@link ApiResponse} dùng chung toàn hệ thống:
 * {@code { "status": "error", "errors": { "field": ["message", ...] } }}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Lỗi @Valid trên @RequestBody (DTO) -> MethodArgumentNotValidException.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.computeIfAbsent(fieldError.getField(), key -> new ArrayList<>())
                    .add(fieldError.getDefaultMessage());
        }
        return buildResponse(errors);
    }

    /**
     * Lỗi validation trên @RequestParam / @PathVariable -> ConstraintViolationException.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, List<String>> errors = new LinkedHashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String field = violation.getPropertyPath().toString();
            errors.computeIfAbsent(field, key -> new ArrayList<>())
                    .add(violation.getMessage());
        }
        return buildResponse(errors);
    }

    /**
     * Body JSON không đọc/parse được (sai kiểu, JSON hỏng, enum không hợp lệ...).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotReadable(HttpMessageNotReadableException ex) {
        Map<String, List<String>> errors = new LinkedHashMap<>();
        errors.put("body", Collections.singletonList("Malformed or unreadable request body"));
        return buildResponse(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException ex) {
        return buildResponse(ex.getStatus(), ex.getErrors());
    }

    private ResponseEntity<ApiResponse<Void>> buildResponse(Map<String, List<String>> errors) {
        return buildResponse(HttpStatus.BAD_REQUEST, errors);
    }

    private ResponseEntity<ApiResponse<Void>> buildResponse(HttpStatus status, Map<String, List<String>> errors) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus("error");
        response.setErrors(errors);
        return ResponseEntity.status(status).body(response);
    }
}
