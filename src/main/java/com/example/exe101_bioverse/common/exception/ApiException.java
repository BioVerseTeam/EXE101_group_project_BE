package com.example.exe101_bioverse.common.exception;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final Map<String, List<String>> errors;

    public ApiException(HttpStatus status, String message) {
        this(status, "general", message);
    }

    public ApiException(HttpStatus status, String field, String message) {
        super(message);
        this.status = status;
        this.errors = new LinkedHashMap<>();
        this.errors.put(field, Collections.singletonList(message));
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }
}
