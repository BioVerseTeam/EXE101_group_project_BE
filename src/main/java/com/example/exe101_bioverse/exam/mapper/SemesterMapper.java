package com.example.exe101_bioverse.exam.mapper;

import com.example.exe101_bioverse.exam.dto.request.SemesterRequest;
import com.example.exe101_bioverse.exam.dto.response.SemesterResponse;
import com.example.exe101_bioverse.exam.entity.Semester;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SemesterMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "grade", ignore = true)
    @Mapping(target = "subjects", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    Semester toEntity(SemesterRequest request);

    @Mapping(target = "classId", source = "grade.id")
    @Mapping(target = "className", source = "grade.name")
    @Mapping(target = "classGrade", source = "grade.grade")
    @Mapping(target = "subjectCount", expression = "java(semester.getSubjects() != null ? semester.getSubjects().size() : 0)")
    SemesterResponse toResponse(Semester semester);
}
