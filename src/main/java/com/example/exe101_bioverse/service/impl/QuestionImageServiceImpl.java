package com.example.exe101_bioverse.service.impl;

import com.example.exe101_bioverse.dto.request.QuestionImageRequest;
import com.example.exe101_bioverse.dto.response.QuestionImageResponse;
import com.example.exe101_bioverse.entity.Question;
import com.example.exe101_bioverse.entity.QuestionImage;
import com.example.exe101_bioverse.mapper.QuestionImageMapper;
import com.example.exe101_bioverse.repository.QuestionImageRepository;
import com.example.exe101_bioverse.service.QuestionImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;

@Service
public class QuestionImageServiceImpl implements QuestionImageService {

    @Autowired
    private QuestionImageMapper questionImageMapper;

    @Autowired
    private QuestionImageRepository questionImageRepository;

    @Override
    public QuestionImageResponse saveQuestionImage(QuestionImageRequest questionImageRequest) {
        if (questionImageRequest.getId() != null) {
            var questionImage = questionImageRepository.findById(questionImageRequest.getId()).orElse(null);
            if (questionImage != null) {
                questionImage.setName(questionImageRequest.getName());
                questionImage.setDisplayOrder(questionImageRequest.getDisplayOrder());
                questionImage.setUrl(questionImageRequest.getUrl());
                questionImage = questionImageRepository.save(questionImage);
                return questionImageMapper.toResponse(questionImage);
            } else {
                throw new RuntimeException("Question image not found with id: " + questionImageRequest.getId());
            }
        } else {
            var questionImage = questionImageMapper.toEntity(questionImageRequest);
            questionImage = questionImageRepository.save(questionImage);
            return questionImageMapper.toResponse(questionImage);
        }
    }

    @Override
    public List<QuestionImage> internalSaveQuestionImage(Question question, List<QuestionImageRequest> questionImageRequests) {
        List<QuestionImage> questionImages = questionImageRequests.stream()
                .map(request -> {
                    QuestionImage questionImage = questionImageMapper.toEntity(request);
                    questionImage.setCreatedDate(java.time.LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                    questionImage.setQuestion(question);
                    return questionImage;
                })
                .toList();
        return questionImageRepository.saveAll(questionImages);
    }

    @Override
    public List<QuestionImageResponse> getQuestionImagesByQuestionId(Long questionId) {
        return questionImageRepository.findByQuestionId(questionId).stream()
                .map(questionImageMapper::toResponse)
                .toList();
    }
}
