package com.example.exe101_bioverse.exam.service;

import com.example.exe101_bioverse.exam.dto.request.SemesterRequest;
import com.example.exe101_bioverse.exam.dto.response.SemesterResponse;

import java.util.List;

public interface SemesterService {
    SemesterResponse createSemester(SemesterRequest request);
    SemesterResponse updateSemester(Long id, SemesterRequest request);
    SemesterResponse getSemesterById(Long id);
    List<SemesterResponse> getSemestersByClassId(Long classId);
    List<SemesterResponse> getAllSemesters();
    void deleteSemester(Long id);
}
