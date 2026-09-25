package com.example.exe101_bioverse.exam.mapper;

import com.example.exe101_bioverse.exam.dto.request.ClassRequest;
import com.example.exe101_bioverse.exam.dto.response.ClassResponse;
import com.example.exe101_bioverse.exam.entity.Grade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GradeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "semesters", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    Grade toEntity(ClassRequest request);

    @Mapping(target = "semesterCount", expression = "java(grade.getSemesters() != null ? grade.getSemesters().size() : 0)")
    ClassResponse toResponse(Grade grade);
}
