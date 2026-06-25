package com.example.exe101_bioverse.service;

import com.example.exe101_bioverse.dto.request.QuestionImageRequest;
import com.example.exe101_bioverse.dto.response.QuestionImageResponse;
import com.example.exe101_bioverse.entity.Question;
import com.example.exe101_bioverse.entity.QuestionImage;

import java.util.List;

public interface QuestionImageService {
    QuestionImageResponse saveQuestionImage(QuestionImageRequest questionImageRequest);
    List<QuestionImage> internalSaveQuestionImage(Question question,List<QuestionImageRequest> questionImageRequests);
    List<QuestionImageResponse> getQuestionImagesByQuestionId(Long questionId);
}
