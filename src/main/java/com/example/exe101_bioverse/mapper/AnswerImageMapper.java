package com.example.exe101_bioverse.mapper;

import com.example.exe101_bioverse.dto.request.AnswerImageRequest;
import com.example.exe101_bioverse.dto.response.AnswerImageResponse;
import com.example.exe101_bioverse.entity.AnswerImage;
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
