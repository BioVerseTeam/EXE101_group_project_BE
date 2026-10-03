package com.example.exe101_bioverse.exam.service.impl;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.exam.dto.request.QuestionRequest;
import com.example.exe101_bioverse.exam.dto.request.QuestionUpdateRequest;
import com.example.exe101_bioverse.exam.dto.response.QuestionResponse;
import com.example.exe101_bioverse.exam.entity.Question;
import com.example.exe101_bioverse.exam.enums.QuestionType;
import com.example.exe101_bioverse.exam.mapper.QuestionMapper;
import com.example.exe101_bioverse.exam.repository.QuestionRepository;
import com.example.exe101_bioverse.exam.service.AnswerService;
import com.example.exe101_bioverse.exam.service.QuestionImageService;
import com.example.exe101_bioverse.exam.service.QuestionService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND, "Không tìm thấy câu hỏi với ID: " + questionId));
    }

    @Override
    public <T> T saveQuestion(QuestionRequest questionRequest, Class<T> returnType) {
        Question question = questionMapper.toEntity(questionRequest);
        question.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        question.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        question = questionRepository.save(question);
        answerService.internalSaveAnswers(question, questionRequest.getAnswers());

        if (returnType == Question.class) {
            return returnType.cast(question);
        } else if (returnType == QuestionResponse.class) {
            return returnType.cast(questionMapper.toResponse(question));
        } else {
            throw new AppException(ErrorCode.UNSUPPORTED_RETURN_TYPE, "Kiểu dữ liệu phản hồi không được hỗ trợ: " + returnType.getName());
        }
    }

    @Override
    public Question internalSaveQuestion(QuestionRequest questionRequest) {
        Question question = new Question();
        question.setContent(questionRequest.getContent());
        question.setType(questionRequest.getType());
        question.setExplain(questionRequest.getExplain());
        question.setDescription(questionRequest.getDescription());
        question.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        question.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        question = questionRepository.save(question);
        question.setAnswers(answerService.internalSaveAnswers(question, questionRequest.getAnswers()));
        question.setQuestionImages(questionImageService.internalSaveQuestionImage(question, questionRequest.getQuestionImageRequests()));
        question = questionRepository.save(question);
        return question;
    }

    @Override
    public List<QuestionResponse> getQuestionsByExamId(Long examId) {
        return questionRepository.findByExamId(examId).stream()
                .map(questionMapper::toResponse)
                .toList();
    }

    @Override
    public Question internalGetById(Long questionId) {
        return questionRepository.findById(questionId).orElse(null);
    }

    @Override
    public List<QuestionResponse> getQuestionByType(QuestionType questionType) {
        return questionRepository.findByType(questionType).stream()
                .map(questionMapper::toResponse)
                .toList();
    }

    @Override
    public PageResponse<QuestionResponse> getQuestionBank(String search, QuestionType type, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), Sort.by(Sort.Direction.DESC, "id"));

        Specification<Question> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (search != null && !search.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("content")), "%" + search.trim().toLowerCase() + "%"));
            }
            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Question> questionPage = questionRepository.findAll(spec, pageable);
        return PageResponse.from(questionPage.map(questionMapper::toResponse));
    }

    @Override
    public QuestionResponse updateQuestion(Long id, QuestionUpdateRequest request) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND, "Không tìm thấy câu hỏi với ID: " + id));

        if (request.getContent() != null) {
            question.setContent(request.getContent());
        }
        if (request.getType() != null) {
            question.setType(request.getType());
        }
        if (request.getExplain() != null) {
            question.setExplain(request.getExplain());
        }
        if (request.getDescription() != null) {
            question.setDescription(request.getDescription());
        }
        question.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        question = questionRepository.save(question);
        return questionMapper.toResponse(question);
    }

    @Override
    public void deleteQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND, "Không tìm thấy câu hỏi với ID: " + id));
        questionRepository.delete(question);
    }
}
