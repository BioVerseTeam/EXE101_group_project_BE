package com.example.exe101_bioverse.exam.service.impl;

import com.example.exe101_bioverse.exam.dto.request.ClassRequest;
import com.example.exe101_bioverse.exam.dto.response.ClassResponse;
import com.example.exe101_bioverse.exam.entity.ClassEntity;
import com.example.exe101_bioverse.exam.mapper.ClassMapper;
import com.example.exe101_bioverse.exam.repository.ClassRepository;
import com.example.exe101_bioverse.exam.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ClassMapper classMapper;

    @Override
    @Transactional
    public ClassResponse createClass(ClassRequest request) {
        if (request.getGrade() == null) {
            throw new IllegalArgumentException("Khối lớp (grade) không được để trống.");
        }
        if (classRepository.existsByGrade(request.getGrade())) {
            throw new IllegalArgumentException("Khối lớp " + request.getGrade() + " đã tồn tại.");
        }
        ClassEntity entity = classMapper.toEntity(request);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setUpdatedDate(LocalDateTime.now());
        ClassEntity saved = classRepository.save(entity);
        return classMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ClassResponse updateClass(Long id, ClassRequest request) {
        ClassEntity entity = classRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khối lớp với ID: " + id));

        if (request.getGrade() != null && !request.getGrade().equals(entity.getGrade())) {
            if (classRepository.existsByGrade(request.getGrade())) {
                throw new IllegalArgumentException("Khối lớp " + request.getGrade() + " đã tồn tại.");
            }
            entity.setGrade(request.getGrade());
        }
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            entity.setName(request.getName());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
        entity.setUpdatedDate(LocalDateTime.now());
        ClassEntity saved = classRepository.save(entity);
        return classMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ClassResponse getClassById(Long id) {
        ClassEntity entity = classRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khối lớp với ID: " + id));
        return classMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ClassResponse getClassByGrade(Integer grade) {
        ClassEntity entity = classRepository.findByGrade(grade)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khối lớp với grade: " + grade));
        return classMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassResponse> getAllClasses() {
        return classRepository.findAllByOrderByGradeAsc().stream()
                .map(classMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteClass(Long id) {
        if (!classRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy khối lớp với ID: " + id);
        }
        classRepository.deleteById(id);
    }
}
