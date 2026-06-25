package com.example.exe101_bioverse.exam.service.impl;

import com.example.exe101_bioverse.exam.dto.request.QuestionRequest;
import com.example.exe101_bioverse.exam.dto.response.QuestionResponse;
import com.example.exe101_bioverse.exam.entity.ExamQuestion;
import com.example.exe101_bioverse.exam.entity.Question;
import com.example.exe101_bioverse.exam.enums.QuestionType;
import com.example.exe101_bioverse.exam.mapper.QuestionMapper;
import com.example.exe101_bioverse.exam.repository.QuestionRepository;
import com.example.exe101_bioverse.exam.service.AnswerService;
import com.example.exe101_bioverse.exam.service.QuestionImageService;
import com.example.exe101_bioverse.exam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionImageService questionImageService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public QuestionResponse getQuestionById(Long questionId) {
        return questionRepository.findById(questionId)
                .map(questionMapper::toResponse)
                .orElse(null);
    }

    @Override
    public <T> T saveQuestion(QuestionRequest questionRequest, Class<T> returnType) {
        Question question = questionMapper.toEntity(questionRequest);
        question.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        question.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        question = questionRepository.save(question);
        answerService.internalSaveAnswers(question, questionRequest.getAnswers());

//        if (questionRequest.getId() != null) {
//            question = questionRepository.findById(questionRequest.getId()).orElse(null);
//            if (question != null) {
//                question.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
//                question.setContent(questionRequest.getContent());
//                question.setType(questionRequest.getType());
//                question.setExplain(questionRequest.getExplain());
//                question.setDescription(questionRequest.getDescription());
//            }else {
//                throw new RuntimeException("Question not found with id: " + questionRequest.getId());
//            }
//            question = questionRepository.save(question);
//        } else {
//            question = questionMapper.toEntity(questionRequest);
//            question.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
//            question.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
//            question = questionRepository.save(question);
//        }


        if (returnType == Question.class) {
            return returnType.cast(question);
        } else if (returnType == QuestionResponse.class) {
            return returnType.cast(questionMapper.toResponse(question));
        } else {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    @Override
    public Question internalSaveQuestion(ExamQuestion examQuestion, QuestionRequest questionRequest) {
        Question question = new Question();
        question.setContent(questionRequest.getContent());
        question.setType(questionRequest.getType());
        question.setExplain(questionRequest.getExplain());
        question.setDescription(questionRequest.getDescription());
        question.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        question.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        question.setExamQuestions(List.of(examQuestion));
        question = questionRepository.save(question);
        question.setAnswers(answerService.internalSaveAnswers(question, questionRequest.getAnswers()));
        question.setQuestionImages(questionImageService.internalSaveQuestionImage(question, questionRequest.getQuestionImageRequests()));
        question = questionRepository.save(question);
        return question;
    }

    @Override
    public List<QuestionResponse> getQuestionsByExamId(Long examId) {
        return List.of();
    }

    @Override
    public Question internalGetById(Long questionId) {
        return questionRepository.findById(questionId).orElse(null);
    }

    @Override
    public List<QuestionResponse> getQuestionByType(QuestionType questionType) {
        return List.of();
    }
}
