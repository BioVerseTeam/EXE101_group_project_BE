package com.example.exe101_bioverse.exam.service.impl;

import com.example.exe101_bioverse.exam.dto.request.SemesterRequest;
import com.example.exe101_bioverse.exam.dto.response.SemesterResponse;
import com.example.exe101_bioverse.exam.entity.ClassEntity;
import com.example.exe101_bioverse.exam.entity.Semester;
import com.example.exe101_bioverse.exam.mapper.SemesterMapper;
import com.example.exe101_bioverse.exam.repository.ClassRepository;
import com.example.exe101_bioverse.exam.repository.SemesterRepository;
import com.example.exe101_bioverse.exam.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private SemesterMapper semesterMapper;

    @Override
    @Transactional
    public SemesterResponse createSemester(SemesterRequest request) {
        if (request.getClassId() == null) {
            throw new IllegalArgumentException("classId không được để trống.");
        }
        ClassEntity classEntity = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khối lớp với ID: " + request.getClassId()));

        Semester semester = semesterMapper.toEntity(request);
        semester.setClassEntity(classEntity);
        semester.setCreatedDate(LocalDateTime.now());
        semester.setUpdatedDate(LocalDateTime.now());

        Semester saved = semesterRepository.save(semester);
        return semesterMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public SemesterResponse updateSemester(Long id, SemesterRequest request) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy học kỳ với ID: " + id));

        if (request.getClassId() != null && !request.getClassId().equals(semester.getClassEntity().getId())) {
            ClassEntity classEntity = classRepository.findById(request.getClassId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khối lớp với ID: " + request.getClassId()));
            semester.setClassEntity(classEntity);
        }
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            semester.setName(request.getName());
        }
        if (request.getSemesterOrder() != null) {
            semester.setSemesterOrder(request.getSemesterOrder());
        }
        if (request.getDescription() != null) {
            semester.setDescription(request.getDescription());
        }
        semester.setUpdatedDate(LocalDateTime.now());

        Semester saved = semesterRepository.save(semester);
        return semesterMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SemesterResponse getSemesterById(Long id) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy học kỳ với ID: " + id));
        return semesterMapper.toResponse(semester);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemesterResponse> getSemestersByClassId(Long classId) {
        return semesterRepository.findByClassEntityIdOrderBySemesterOrderAsc(classId).stream()
                .map(semesterMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemesterResponse> getAllSemesters() {
        return semesterRepository.findAll().stream()
                .map(semesterMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteSemester(Long id) {
        if (!semesterRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy học kỳ với ID: " + id);
        }
        semesterRepository.deleteById(id);
    }
}
