package com.example.exe101_bioverse.mapper;

import com.example.exe101_bioverse.dto.request.AnswerRequest;
import com.example.exe101_bioverse.dto.response.AnswerResponse;
import com.example.exe101_bioverse.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "answerImages", ignore = true)
    Answer toEntity(AnswerRequest answerRequest);

    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "answerImageResponses", source = "answerImages")
    AnswerResponse toResponse(Answer answer);
}
