package com.example.exe101_bioverse.exam.service;

import com.example.exe101_bioverse.exam.dto.request.SubjectRequest;
import com.example.exe101_bioverse.exam.dto.response.SubjectResponse;

import java.util.List;

public interface SubjectService {
    SubjectResponse createSubject(SubjectRequest request);
    SubjectResponse updateSubject(Long id, SubjectRequest request);
    SubjectResponse getSubjectById(Long id);
    List<SubjectResponse> getSubjectsBySemesterId(Long semesterId);
    List<SubjectResponse> getAllSubjects();
    void deleteSubject(Long id);
}
