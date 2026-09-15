package com.example.exe101_bioverse.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"data", "code", "message"})
public class ApiResponse<T> {

    private T data;
    private int code;
    private String message;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, 1000, "Thành công");
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, 1000, message);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(null, code, message);
    }

    public static <T> ApiResponse<T> error(int code, String message, T data) {
        return new ApiResponse<>(data, code, message);
    }
}
