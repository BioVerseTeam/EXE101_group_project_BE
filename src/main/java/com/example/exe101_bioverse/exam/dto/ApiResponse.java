package com.example.exe101_bioverse.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse <T>{
    T payload;
    String status;
    Map<String, List<String>> errors;
    Map<String,Object> metadata;
}
