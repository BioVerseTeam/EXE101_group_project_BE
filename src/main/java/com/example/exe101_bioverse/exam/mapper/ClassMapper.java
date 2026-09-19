package com.example.exe101_bioverse.exam.mapper;

import com.example.exe101_bioverse.exam.dto.request.ClassRequest;
import com.example.exe101_bioverse.exam.dto.response.ClassResponse;
import com.example.exe101_bioverse.exam.entity.ClassEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClassMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "semesters", ignore = true)
    ClassEntity toEntity(ClassRequest request);

    @Mapping(target = "semesterCount", expression = "java(classEntity.getSemesters() != null ? classEntity.getSemesters().size() : 0)")
    ClassResponse toResponse(ClassEntity classEntity);
}
