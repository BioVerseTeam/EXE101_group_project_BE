package com.example.exe101_bioverse.exam.mapper;

import com.example.exe101_bioverse.exam.dto.request.AnswerImageRequest;
import com.example.exe101_bioverse.exam.dto.response.AnswerImageResponse;
import com.example.exe101_bioverse.exam.entity.AnswerImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnswerImageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "answer", ignore = true)
    AnswerImage toEntity(AnswerImageRequest answerImageRequest);

    @Mapping(target = "answerId", source = "answer.id")
    AnswerImageResponse toResponse(AnswerImage answerImage);
}
