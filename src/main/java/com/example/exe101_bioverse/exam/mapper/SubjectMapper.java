package com.example.exe101_bioverse.exam.mapper;

import com.example.exe101_bioverse.exam.dto.request.SubjectRequest;
import com.example.exe101_bioverse.exam.dto.response.SubjectResponse;
import com.example.exe101_bioverse.exam.entity.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "semester", ignore = true)
    @Mapping(target = "exams", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    Subject toEntity(SubjectRequest request);

    @Mapping(target = "semesterId", source = "semester.id")
    @Mapping(target = "semesterName", source = "semester.name")
    @Mapping(target = "classId", source = "semester.classEntity.id")
    @Mapping(target = "className", source = "semester.classEntity.name")
    @Mapping(target = "examCount", expression = "java(subject.getExams() != null ? subject.getExams().size() : 0)")
    SubjectResponse toResponse(Subject subject);
}
