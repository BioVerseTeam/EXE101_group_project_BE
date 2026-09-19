package com.example.exe101_bioverse.exam.service;

import com.example.exe101_bioverse.exam.dto.request.ClassRequest;
import com.example.exe101_bioverse.exam.dto.response.ClassResponse;

import java.util.List;

public interface ClassService {
    ClassResponse createClass(ClassRequest request);
    ClassResponse updateClass(Long id, ClassRequest request);
    ClassResponse getClassById(Long id);
    ClassResponse getClassByGrade(Integer grade);
    List<ClassResponse> getAllClasses();
    void deleteClass(Long id);
}
