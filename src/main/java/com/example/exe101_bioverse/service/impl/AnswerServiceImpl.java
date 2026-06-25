package com.example.exe101_bioverse.service.impl;

import com.example.exe101_bioverse.dto.request.AnswerRequest;
import com.example.exe101_bioverse.dto.response.AnswerResponse;
import com.example.exe101_bioverse.entity.Answer;
import com.example.exe101_bioverse.entity.Question;
import com.example.exe101_bioverse.mapper.AnswerMapper;
import com.example.exe101_bioverse.repository.AnswerRepository;
import com.example.exe101_bioverse.service.AnswerImageService;
import com.example.exe101_bioverse.service.AnswerService;
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
    private AnswerMapper answerMapper;

    @Autowired
    private AnswerImageService  answerImageService;


    @Override
    public AnswerResponse saveAnswer(AnswerRequest answerRequest) {
        Answer answer = null;
        if (answerRequest.getId() != null) {
            answer = answerRepository.findById(answerRequest.getId()).orElse(null);
            if (answer != null) {
                answer.setContent(answerRequest.getContent());
                answer.setExplain(answerRequest.getExplain());
                answer.setDescription(answerRequest.getDescription());
                answer.setCorrect(answerRequest.isCorrect());
                answer =  answerRepository.save(answer);
            }else {
                throw new RuntimeException("Answer not found with id: " + answerRequest.getId());
            }
        } else {
            answer = answerMapper.toEntity(answerRequest);
            answer = answerRepository.save(answer);
        }
        return answerMapper.toResponse(answer);
    }

    @Override
    public List<Answer> internalSaveAnswers(Question question, List<AnswerRequest> answerRequestList) {
        List<Answer> answerList = new ArrayList<>();

        for (AnswerRequest answerRequest : answerRequestList) {
            Answer answer = answerMapper.toEntity(answerRequest);

            answer.setQuestion(question);

            answer.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            answer.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));

            answer = answerRepository.save(answer);

            answer.setAnswerImages(answerImageService.internalSaveAnswerImage(answer,answerRequest.getAnswerImageRequests())); // Initialize the answerImages list


            answerList.add(answerRepository.save(answer));
        }
        return answerList;
    }

    @Override
    public List<AnswerResponse> getAnswersByQuestionId(Long questionId) {
        return answerRepository.findByQuestionId(questionId).stream()
                .map(answerMapper::toResponse)
                .toList();
    }
}
