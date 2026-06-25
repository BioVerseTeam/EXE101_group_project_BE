package com.example.exe101_bioverse.service.impl;

import com.example.exe101_bioverse.dto.request.ExamQuestionRequest;
import com.example.exe101_bioverse.dto.request.QuestionRequest;
import com.example.exe101_bioverse.dto.response.ExamQuestionResponse;
import com.example.exe101_bioverse.entity.Exam;
import com.example.exe101_bioverse.entity.ExamQuestion;
import com.example.exe101_bioverse.mapper.ExamQuestionMapper;
import com.example.exe101_bioverse.repository.ExamQuestionRepository;
import com.example.exe101_bioverse.service.ExamQuestionService;
import com.example.exe101_bioverse.service.ExamService;
import com.example.exe101_bioverse.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExamQuestionServiceImpl implements ExamQuestionService {

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamQuestionMapper examQuestionMapper;

    @Override
    public ExamQuestionResponse saveExamQuestion(ExamQuestionRequest examQuestionRequest) {
        ExamQuestion examQuestion = null;
        if (examQuestionRequest.getId() != null) {
            examQuestion = examQuestionRepository.findById(examQuestionRequest.getId()).orElse(null);
            if (examQuestion != null) {
                examQuestion.setPoint(examQuestionRequest.getPoint());
                examQuestion.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                examQuestion.setQuestionOrder(examQuestionRequest.getQuestionOrder());
                examQuestion.setQuestion(questionService.internalGetById(examQuestionRequest.getQuestionId()));
                examQuestion.setExam(examService.internalGetById(examQuestionRequest.getExamId()));
                examQuestion = examQuestionRepository.save(examQuestion);
            }else {
                throw new RuntimeException("ExamQuestion with id " + examQuestionRequest.getId() + " not found");
            }
        } else {
            examQuestion = examQuestionMapper.toEntity(examQuestionRequest);
            examQuestion.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            examQuestion.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            examQuestion.setQuestion(questionService.internalGetById(examQuestionRequest.getQuestionId()));
            examQuestion.setExam(examService.internalGetById(examQuestionRequest.getExamId()));
            examQuestion = examQuestionRepository.save(examQuestion);
        }
        return  examQuestionMapper.toResponse(examQuestion);
    }

    @Override
    public List<ExamQuestion> internalSaveExamQuestion(Exam exam,List<QuestionRequest> questionRequest) {
        List<ExamQuestion> examQuestions = new ArrayList<>();
        if (questionRequest == null || questionRequest.isEmpty()) return examQuestions;

        for (QuestionRequest questionReq : questionRequest) {
            ExamQuestion examQuestion = new ExamQuestion();
            examQuestion.setExam(exam);
            examQuestion.setPoint(questionReq.getPoint());
            examQuestion.setQuestionOrder(questionReq.getQuestionOrder());
            examQuestion.setQuestion(questionService.internalSaveQuestion(examQuestion, questionReq));
            examQuestion.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            examQuestion.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            examQuestionRepository.save(examQuestion);
            examQuestions.add(examQuestion);
        }
        return examQuestions;
    }
    @Override
    public List<ExamQuestionResponse> getExamQuestionsByExamId(Long examId) {
        return examQuestionRepository.findByExamId(examId)
                .stream()
                .map(examQuestionMapper::toResponse)
                .toList();
    }
}
