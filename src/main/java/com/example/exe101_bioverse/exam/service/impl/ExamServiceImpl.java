package com.example.exe101_bioverse.exam.service.impl;

import com.example.exe101_bioverse.exam.dto.request.ExamRequest;
import com.example.exe101_bioverse.exam.dto.response.ExamResponse;
import com.example.exe101_bioverse.exam.entity.Exam;
import com.example.exe101_bioverse.exam.enums.ExamType;
import com.example.exe101_bioverse.exam.mapper.ExamMapper;
import com.example.exe101_bioverse.exam.repository.ExamRepository;
import com.example.exe101_bioverse.exam.service.ExamQuestionService;
import com.example.exe101_bioverse.exam.service.ExamService;
import com.example.exe101_bioverse.exam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ExamQuestionService examQuestionService;

    @Autowired
    private ExamMapper examMapper;

    @Override
    public <T> T saveExam(ExamRequest examRequest, Class<T> returnType) {
        Exam exam;
        if (examRequest.getId() != null) {
            exam = examRepository.findById(examRequest.getId()).orElse(null);
            if (exam != null) {
                exam.setCode(examRequest.getCode());
                exam.setType(examRequest.getType());
                exam.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                exam.setName(examRequest.getName());
                exam.setSubjectName(examRequest.getSubjectName());
                exam.setDescription(examRequest.getDescription());
                exam = examRepository.save(exam);
            } else {
                // Handle the case where the exam with the given ID does not exist
                throw new RuntimeException("Exam with ID " + examRequest.getId() + " not found.");
            }
        } else {
            exam = examMapper.toEntity(examRequest);
            exam.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            exam.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            exam = examRepository.save(exam);
            exam.setExamQuestions(examQuestionService.internalSaveExamQuestion(exam, examRequest.getQuestions()));
        }

        if (returnType == Exam.class) {
            return returnType.cast(exam);
        } else if (returnType == ExamResponse.class) {
            return returnType.cast(examMapper.toResponse(exam));
        } else {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    @Override
    public List<ExamResponse> getAllExams() {
        return examRepository.findAll().stream()
                .map(examMapper::toResponse)
                .toList();
    }

    @Override
    public Exam internalGetById(Long id) {
        return examRepository.findById(id).orElse(null);
    }

    @Override
    public List<ExamResponse> getExamsBySubjectName(String subjectName) {
        return examRepository.findBySubjectName(subjectName).stream()
                .map(examMapper::toResponse)
                .toList();
    }

    @Override
    public List<ExamResponse> getExamsByType(String type) {
        return examRepository.findByType(Enum.valueOf(ExamType.class, type)).stream()
                .map(examMapper::toResponse)
                .toList();
    }

    @Override
    public List<ExamResponse> getExamsByName(String name) {
        return examRepository.findByName(name).stream()
                .map(examMapper::toResponse)
                .toList();
    }

    @Override
    public List<ExamResponse> getExamsByCode(String code) {
        return examRepository.findByCode(code).stream()
                .map(examMapper::toResponse)
                .toList();
    }
}
