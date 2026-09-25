package com.example.exe101_bioverse.exam.service.impl;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.exam.dto.request.ClassRequest;
import com.example.exe101_bioverse.exam.dto.response.ClassResponse;
import com.example.exe101_bioverse.exam.entity.Grade;
import com.example.exe101_bioverse.exam.mapper.GradeMapper;
import com.example.exe101_bioverse.exam.repository.GradeRepository;
import com.example.exe101_bioverse.exam.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements GradeService {

    private final GradeRepository classRepository;
    private final GradeMapper classMapper;

    @Override
    @Transactional
    public ClassResponse createClass(ClassRequest request) {
        if (request.getGrade() == null) {
            throw new AppException(ErrorCode.INVALID_DATA, "Khối lớp (grade) không được để trống.");
        }
        if (classRepository.findByGrade(request.getGrade()).isPresent()) {
            throw new AppException(ErrorCode.CLASS_GRADE_EXISTS, "Khối lớp " + request.getGrade() + " đã tồn tại trong hệ thống.");
        }

        Grade entity = classMapper.toEntity(request);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setUpdatedDate(LocalDateTime.now());
        Grade saved = classRepository.save(entity);
        return classMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ClassResponse updateClass(Long id, ClassRequest request) {
        Grade entity = classRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND, "Không tìm thấy khối lớp với ID: " + id));

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            entity.setName(request.getName());
        }
        if (request.getGrade() != null && !request.getGrade().equals(entity.getGrade())) {
            if (classRepository.findByGrade(request.getGrade()).isPresent()) {
                throw new AppException(ErrorCode.CLASS_GRADE_EXISTS, "Khối lớp " + request.getGrade() + " đã tồn tại trong hệ thống.");
            }
            entity.setGrade(request.getGrade());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
        entity.setUpdatedDate(LocalDateTime.now());

        Grade saved = classRepository.save(entity);
        return classMapper.toResponse(saved);
    }

    @Override
    public ClassResponse getClassById(Long id) {
        Grade entity = classRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND, "Không tìm thấy khối lớp với ID: " + id));
        return classMapper.toResponse(entity);
    }

    @Override
    public ClassResponse getClassByGrade(Integer grade) {
        Grade entity = classRepository.findByGrade(grade)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND, "Không tìm thấy khối lớp: " + grade));
        return classMapper.toResponse(entity);
    }

    @Override
    public List<ClassResponse> getAllClasses() {
        return classRepository.findAllByOrderByGradeAsc().stream()
                .map(classMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteClass(Long id) {
        if (!classRepository.existsById(id)) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND, "Không tìm thấy khối lớp với ID: " + id);
        }
        classRepository.deleteById(id);
    }
}
