package com.example.exe101_bioverse.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse <T>{
    private T payload;
    private String status;
    private Map<String, List<String>> errors;
    private Map<String, Object> metadata;
}
