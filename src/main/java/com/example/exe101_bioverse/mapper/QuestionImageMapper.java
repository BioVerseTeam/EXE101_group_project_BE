package com.example.exe101_bioverse.mapper;

import com.example.exe101_bioverse.dto.request.QuestionImageRequest;
import com.example.exe101_bioverse.dto.response.QuestionImageResponse;
import com.example.exe101_bioverse.entity.QuestionImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") //componentModel = "spring" giúp bạn có thể @Autowired mapper này
public interface QuestionImageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "question", ignore = true)
    QuestionImage toEntity(QuestionImageRequest questionImageRequest);

    @Mapping(target = "questionId", source = "question.id")
    QuestionImageResponse toResponse(QuestionImage questionImage);
}
