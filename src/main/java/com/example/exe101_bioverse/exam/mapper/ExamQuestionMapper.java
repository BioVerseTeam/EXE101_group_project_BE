package com.example.exe101_bioverse.exam.mapper;

import com.example.exe101_bioverse.exam.dto.request.ExamQuestionRequest;
import com.example.exe101_bioverse.exam.dto.response.ExamQuestionResponse;
import com.example.exe101_bioverse.exam.entity.ExamQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExamQuestionMapper {

    // Target ở đây là ExamQuestion Entity, ignore = true có nghĩa là không ánh xạ trường đó từ request vào entity
    @Mapping(target = "exam", ignore = true)     // Sửa từ examId -> exam
    @Mapping(target = "question", ignore = true) // Sửa từ questionId -> question
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    ExamQuestion toEntity(ExamQuestionRequest request);

    // Target ở đây là ExamQuestionResponse DTO
    @Mapping(target = "examId", source = "exam.id")         // Sửa từ exam -> examId (lấy từ entity.exam.id)
    @Mapping(target = "questionId", source = "question.id") // Sửa từ question -> questionId (lấy từ entity.question.id)
    ExamQuestionResponse toResponse(ExamQuestion entity);
}
