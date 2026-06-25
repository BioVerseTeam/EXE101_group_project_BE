package com.example.exe101_bioverse.exam.mapper;

import com.example.exe101_bioverse.exam.dto.request.QuestionRequest;
import com.example.exe101_bioverse.exam.dto.response.QuestionResponse;
import com.example.exe101_bioverse.exam.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") //componentModel = "spring" giúp bạn có thể @Autowired mapper này
public interface QuestionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "examQuestions", ignore = true)
    @Mapping(target = "answers", ignore = true)
    @Mapping(target = "questionImages", ignore = true)
    Question toEntity(QuestionRequest questionRequest);

    @Mapping(target = "questionImageResponses", source = "questionImages")
    QuestionResponse toResponse(Question question);
}
