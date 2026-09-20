package com.example.exe101_bioverse.exam.mapper;

import com.example.exe101_bioverse.exam.dto.request.ExamRequest;
import com.example.exe101_bioverse.exam.dto.response.ExamResponse;
import com.example.exe101_bioverse.exam.entity.Exam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class}) //componentModel = "spring" giúp bạn có thể @Autowired mapper này
public interface ExamMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "examQuestions", ignore = true)
    Exam toEntity(ExamRequest examRequest);

    @Mapping(target = "questions", ignore = true)
    ExamResponse toResponse(Exam exam);
}
