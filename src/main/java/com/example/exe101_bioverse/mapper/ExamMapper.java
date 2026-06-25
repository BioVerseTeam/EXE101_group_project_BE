package com.example.exe101_bioverse.mapper;

import com.example.exe101_bioverse.dto.request.ExamRequest;
import com.example.exe101_bioverse.dto.response.ExamResponse;
import com.example.exe101_bioverse.entity.Exam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") //componentModel = "spring" giúp bạn có thể @Autowired mapper này
public interface ExamMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "examQuestions", ignore = true)
    Exam toEntity(ExamRequest examRequest);

    @Mapping(target = "questions", ignore = true)
    ExamResponse toResponse(Exam exam);
}
