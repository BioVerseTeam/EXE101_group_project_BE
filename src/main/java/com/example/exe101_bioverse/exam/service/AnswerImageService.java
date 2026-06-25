package com.example.exe101_bioverse.exam.service;

import com.example.exe101_bioverse.exam.dto.request.AnswerImageRequest;
import com.example.exe101_bioverse.exam.dto.response.AnswerImageResponse;
import com.example.exe101_bioverse.exam.entity.Answer;
import com.example.exe101_bioverse.exam.entity.AnswerImage;

import java.util.List;

public interface AnswerImageService {
    AnswerImageResponse saveAnswerImage(AnswerImageRequest request);
    List<AnswerImage> internalSaveAnswerImage(Answer answer, List<AnswerImageRequest> request);
    List<AnswerImageResponse> getAnswerImagesByAnswerId(Long AnswerId);
}
