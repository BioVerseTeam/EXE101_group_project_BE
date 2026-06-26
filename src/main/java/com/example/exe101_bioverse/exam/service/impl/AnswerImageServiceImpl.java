package com.example.exe101_bioverse.exam.service.impl;

import com.example.exe101_bioverse.exam.dto.request.AnswerImageRequest;
import com.example.exe101_bioverse.exam.dto.response.AnswerImageResponse;
import com.example.exe101_bioverse.exam.entity.Answer;
import com.example.exe101_bioverse.exam.entity.AnswerImage;
import com.example.exe101_bioverse.exam.mapper.AnswerImageMapper;
import com.example.exe101_bioverse.exam.repository.AnswerImageRepository;
import com.example.exe101_bioverse.exam.service.AnswerImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerImageServiceImpl implements AnswerImageService {

    @Autowired
    private AnswerImageRepository answerImageRepository;

    @Autowired
    private AnswerImageMapper answerImageMapper;

    @Override
    public AnswerImageResponse saveAnswerImage(AnswerImageRequest request) {
        AnswerImage answerImage = null;
        if (request.getId() != null) {
            answerImage = answerImageRepository.findById(request.getId()).orElse(null);
            if (answerImage != null) {
                answerImage.setUrl(request.getUrl());
                answerImage.setDisplayOrder(request.getDisplayOrder());
                answerImage.setName(request.getName());
                answerImage = answerImageRepository.save(answerImage);
            } else {
                throw new RuntimeException("Answer image not found with id: " + request.getId());
            }
        } else {
            answerImage = answerImageMapper.toEntity(request);
            answerImage.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            answerImage = answerImageRepository.save(answerImage);
        }
        return answerImageMapper.toResponse(answerImage);
    }

    @Override
    public List<AnswerImage> internalSaveAnswerImage(Answer answer, List<AnswerImageRequest> request) {
        List<AnswerImage> answerImages = new ArrayList<>();
        if (answer.getAnswerImages() == null) {
            answer.setAnswerImages(new ArrayList<>());
        }
        for (AnswerImageRequest answerImageRequest : request) {
            AnswerImage answerImage = answerImageMapper.toEntity(answerImageRequest);
            answerImage.setAnswer(answer);
            answer.getAnswerImages().add(answerImage);
            answerImage.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            answerImage = answerImageRepository.save(answerImage);
            answerImages.add(answerImage);
        }
        return answerImages;
    }

    @Override
    public List<AnswerImageResponse> getAnswerImagesByAnswerId(Long AnswerId) {
        return answerImageRepository.getAnswerImagesByAnswerId(AnswerId).stream()
                .map(answerImageMapper::toResponse)
                .toList();
    }


}
