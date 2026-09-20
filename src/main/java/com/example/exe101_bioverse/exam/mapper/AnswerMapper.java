package com.example.exe101_bioverse.exam.mapper;

import com.example.exe101_bioverse.exam.dto.request.AnswerRequest;
import com.example.exe101_bioverse.exam.dto.response.AnswerResponse;
import com.example.exe101_bioverse.exam.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AnswerImageMapper.class})
public interface AnswerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "answerImages", ignore = true)
    Answer toEntity(AnswerRequest answerRequest);

    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "answerImageResponses", source = "answerImages")
    @Mapping(target = "isCorrect", source = "correct")
    AnswerResponse toResponse(Answer answer);
}
