package com.example.exe101_bioverse.exam.service.impl;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.exam.dto.request.SubjectRequest;
import com.example.exe101_bioverse.exam.dto.response.SubjectResponse;
import com.example.exe101_bioverse.exam.entity.Semester;
import com.example.exe101_bioverse.exam.entity.Subject;
import com.example.exe101_bioverse.exam.mapper.SubjectMapper;
import com.example.exe101_bioverse.exam.repository.SemesterRepository;
import com.example.exe101_bioverse.exam.repository.SubjectRepository;
import com.example.exe101_bioverse.exam.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private SubjectMapper subjectMapper;

    @Override
    @Transactional
    public SubjectResponse createSubject(SubjectRequest request) {
        if (request.getSemesterId() == null) {
            throw new AppException(ErrorCode.INVALID_DATA, "semesterId không được để trống.");
        }
        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND, "Không tìm thấy học kỳ với ID: " + request.getSemesterId()));

        Subject subject = subjectMapper.toEntity(request);
        subject.setSemester(semester);
        subject.setCreatedDate(LocalDateTime.now());
        subject.setUpdatedDate(LocalDateTime.now());

        Subject saved = subjectRepository.save(subject);
        return subjectMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public SubjectResponse updateSubject(Long id, SubjectRequest request) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND, "Không tìm thấy môn học với ID: " + id));

        if (request.getSemesterId() != null && !request.getSemesterId().equals(subject.getSemester().getId())) {
            Semester semester = semesterRepository.findById(request.getSemesterId())
                    .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND, "Không tìm thấy học kỳ với ID: " + request.getSemesterId()));
            subject.setSemester(semester);
        }
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            subject.setName(request.getName());
        }
        if (request.getCode() != null) {
            subject.setCode(request.getCode());
        }
        if (request.getDescription() != null) {
            subject.setDescription(request.getDescription());
        }
        subject.setUpdatedDate(LocalDateTime.now());

        Subject saved = subjectRepository.save(subject);
        return subjectMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectResponse getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND, "Không tìm thấy môn học với ID: " + id));
        return subjectMapper.toResponse(subject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectResponse> getSubjectsBySemesterId(Long semesterId) {
        return subjectRepository.findBySemesterId(semesterId).stream()
                .map(subjectMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(subjectMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new AppException(ErrorCode.SUBJECT_NOT_FOUND, "Không tìm thấy môn học với ID: " + id);
        }
        subjectRepository.deleteById(id);
    }
}
