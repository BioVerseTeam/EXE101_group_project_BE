package com.example.exe101_bioverse.exam.service.impl;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.exam.dto.request.AnswerRequest;
import com.example.exe101_bioverse.exam.dto.response.AnswerResponse;
import com.example.exe101_bioverse.exam.entity.Answer;
import com.example.exe101_bioverse.exam.entity.Question;
import com.example.exe101_bioverse.exam.mapper.AnswerMapper;
import com.example.exe101_bioverse.exam.repository.AnswerRepository;
import com.example.exe101_bioverse.exam.repository.QuestionRepository;
import com.example.exe101_bioverse.exam.service.AnswerImageService;
import com.example.exe101_bioverse.exam.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private AnswerImageService answerImageService;

    @Override
    public AnswerResponse saveAnswer(AnswerRequest answerRequest) {
        Answer answer = null;
        if (answerRequest.getId() != null) {
            answer = answerRepository.findById(answerRequest.getId()).orElse(null);
            if (answer != null) {
                if (answerRequest.getType() != null) {
                    answer.setType(answerRequest.getType());
                }
                answer.setContent(answerRequest.getContent());
                answer.setExplain(answerRequest.getExplain());
                answer.setDescription(answerRequest.getDescription());
                answer.setCorrect(answerRequest.isCorrect());
                answer.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                answer = answerRepository.save(answer);
            } else {
                throw new AppException(ErrorCode.ANSWER_NOT_FOUND, "Không tìm thấy đáp án với ID: " + answerRequest.getId());
            }
        } else {
            answer = answerMapper.toEntity(answerRequest);
            if (answerRequest.getQuestionId() != null) {
                Question question = questionRepository.findById(answerRequest.getQuestionId())
                        .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND, "Không tìm thấy câu hỏi với ID: " + answerRequest.getQuestionId()));
                answer.setQuestion(question);
            }
            answer.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            answer.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            answer = answerRepository.save(answer);
        }
        return answerMapper.toResponse(answer);
    }

    @Override
    public List<Answer> internalSaveAnswers(Question question, List<AnswerRequest> answerRequestList) {
        List<Answer> answerList = new ArrayList<>();
        if (answerRequestList == null) {
            return answerList;
        }

        for (AnswerRequest answerRequest : answerRequestList) {
            Answer answer = answerMapper.toEntity(answerRequest);
            answer.setQuestion(question);
            answer.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            answer.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            answer = answerRepository.save(answer);

            if (answerRequest.getAnswerImageRequests() != null) {
                answer.setAnswerImages(answerImageService.internalSaveAnswerImage(answer, answerRequest.getAnswerImageRequests()));
                answer = answerRepository.save(answer);
            }

            answerList.add(answer);
        }
        return answerList;
    }

    @Override
    public List<AnswerResponse> getAnswersByQuestionId(Long questionId) {
        return answerRepository.findByQuestionId(questionId).stream()
                .map(answerMapper::toResponse)
                .toList();
    }

    @Override
    public AnswerResponse addAnswerToQuestion(Long questionId, AnswerRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND, "Không tìm thấy câu hỏi với ID: " + questionId));
        Answer answer = answerMapper.toEntity(request);
        answer.setQuestion(question);
        answer.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        answer.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        answer = answerRepository.save(answer);

        if (request.getAnswerImageRequests() != null && !request.getAnswerImageRequests().isEmpty()) {
            answer.setAnswerImages(answerImageService.internalSaveAnswerImage(answer, request.getAnswerImageRequests()));
            answer = answerRepository.save(answer);
        }

        return answerMapper.toResponse(answer);
    }

    @Override
    public AnswerResponse updateAnswer(Long id, AnswerRequest request) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND, "Không tìm thấy đáp án với ID: " + id));

        if (request.getType() != null) {
            answer.setType(request.getType());
        }
        if (request.getContent() != null) {
            answer.setContent(request.getContent());
        }
        if (request.getExplain() != null) {
            answer.setExplain(request.getExplain());
        }
        if (request.getDescription() != null) {
            answer.setDescription(request.getDescription());
        }
        if (request.getIsCorrect() != null) {
            answer.setCorrect(request.isCorrect());
        }
        answer.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        answer = answerRepository.save(answer);
        return answerMapper.toResponse(answer);
    }

    @Override
    public void deleteAnswer(Long id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND, "Không tìm thấy đáp án với ID: " + id));
        answerRepository.delete(answer);
    }
}
