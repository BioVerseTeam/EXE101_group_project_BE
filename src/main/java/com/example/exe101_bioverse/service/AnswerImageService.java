package com.example.exe101_bioverse.service;

import com.example.exe101_bioverse.dto.request.AnswerImageRequest;
import com.example.exe101_bioverse.dto.response.AnswerImageResponse;
import com.example.exe101_bioverse.entity.Answer;
import com.example.exe101_bioverse.entity.AnswerImage;

import java.util.List;

public interface AnswerImageService {
    AnswerImageResponse saveAnswerImage(AnswerImageRequest request);
    List<AnswerImage> internalSaveAnswerImage(Answer answer, List<AnswerImageRequest> request);
    List<AnswerImageResponse> getAnswerImagesByQuestionId(Long questionId);
}
