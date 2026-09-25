package com.example.exe101_bioverse.exam.service;

import com.example.exe101_bioverse.exam.dto.request.QuestionImageRequest;
import com.example.exe101_bioverse.exam.dto.response.QuestionImageResponse;
import com.example.exe101_bioverse.exam.entity.Question;
import com.example.exe101_bioverse.exam.entity.QuestionImage;

import java.util.List;

public interface QuestionImageService {
    QuestionImageResponse saveQuestionImage(QuestionImageRequest questionImageRequest);
    List<QuestionImage> internalSaveQuestionImage(Question question, List<QuestionImageRequest> questionImageRequests);
    List<QuestionImageResponse> getQuestionImagesByQuestionId(Long questionId);
    void deleteQuestionImage(Long id);
}
