package com.example.exe101_bioverse.exam.service.impl;

import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.exam.dto.request.*;
import com.example.exe101_bioverse.exam.dto.response.*;
import com.example.exe101_bioverse.exam.entity.*;
import com.example.exe101_bioverse.exam.enums.AnswerType;
import com.example.exe101_bioverse.exam.enums.ExamType;
import com.example.exe101_bioverse.exam.enums.QuestionType;
import com.example.exe101_bioverse.exam.mapper.ExamMapper;
import com.example.exe101_bioverse.exam.mapper.ExamQuestionMapper;
import com.example.exe101_bioverse.exam.mapper.QuestionMapper;
import com.example.exe101_bioverse.exam.repository.*;
import com.example.exe101_bioverse.exam.service.ExamQuestionService;
import com.example.exe101_bioverse.exam.service.ExamService;
import com.example.exe101_bioverse.exam.service.QuestionService;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
public class ExamServiceImpl implements ExamService {

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    @Autowired(required = false)
    private ObjectMapper objectMapper;

    private static final Duration CATALOG_CACHE_TTL = Duration.ofMinutes(15);
    private static final Duration ALL_EXAMS_CACHE_TTL = Duration.ofMinutes(15);
    private static final Duration SUMMARY_CACHE_TTL = Duration.ofMinutes(30);

    @PostConstruct
    public void init() {
        if (this.objectMapper == null) {
            this.objectMapper = new ObjectMapper();
        }
        this.objectMapper.findAndRegisterModules();
    }

    private void evictExamCache() {
        if (redisTemplate == null) return;
        try {
            redisTemplate.delete("exam:all");
            redisTemplate.delete("exam:summary");
            Set<String> keys = redisTemplate.keys("exam:catalog:*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
            log.info("[Redis Cache] Đã xóa cache cho đề thi (exam:all, exam:summary, {} catalog keys)", keys != null ? keys.size() : 0);
        } catch (Exception e) {
            log.warn("[Redis Cache] Lỗi khi xóa cache đề thi: {}", e.getMessage());
        }
    }

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionImageRepository questionImageRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ExamQuestionService examQuestionService;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ExamQuestionMapper examQuestionMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public <T> T saveExam(ExamRequest examRequest, Class<T> returnType) {
        Exam exam;
        if (examRequest.getId() != null) {
            exam = examRepository.findById(examRequest.getId()).orElse(null);
            if (exam != null) {
                exam.setCode(examRequest.getCode());
                exam.setType(examRequest.getType());
                exam.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                exam.setName(examRequest.getName());
                exam.setSubjectName(examRequest.getSubjectName());
                exam.setDescription(examRequest.getDescription());
                exam = examRepository.save(exam);
            } else {
                throw new AppException(ErrorCode.EXAM_NOT_FOUND, "Không tìm thấy đề thi với ID: " + examRequest.getId());
            }
        } else {
            exam = examMapper.toEntity(examRequest);
            exam.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            exam.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            if (exam.getDurationMinutes() == null) {
                exam.setDurationMinutes(45);
            }
            if (exam.getTotalScore() == null) {
                exam.setTotalScore(10.0);
            }
            if (exam.getIsActive() == null) {
                exam.setIsActive(true);
            }
            exam = examRepository.save(exam);
            if (examRequest.getQuestions() != null && !examRequest.getQuestions().isEmpty()) {
                exam.setExamQuestions(examQuestionService.internalSaveExamQuestion(exam, examRequest.getQuestions()));
            }
        }

        evictExamCache();

        if (returnType == Exam.class) {
            return returnType.cast(exam);
        } else if (returnType == ExamResponse.class) {
            return returnType.cast(examMapper.toResponse(exam));
        } else {
            throw new AppException(ErrorCode.UNSUPPORTED_RETURN_TYPE, "Kiểu dữ liệu phản hồi không được hỗ trợ: " + returnType.getName());
        }
    }

    @Override
    public List<ExamResponse> getAllExams() {
        if (redisTemplate != null && objectMapper != null) {
            try {
                String cached = redisTemplate.opsForValue().get("exam:all");
                if (cached != null && !cached.isBlank()) {
                    JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, ExamResponse.class);
                    List<ExamResponse> list = objectMapper.readValue(cached, type);
                    log.info("[Redis Cache HIT] getAllExams -> {} đề thi", list.size());
                    return list;
                }
            } catch (Exception e) {
                log.warn("[Redis Cache] Lỗi đọc exam:all: {}", e.getMessage());
            }
        }

        List<ExamResponse> result = examRepository.findAll().stream()
                .map(exam -> {
                    ExamResponse response = examMapper.toResponse(exam);
                    List<QuestionResponse> questions = questionService.getQuestionsByExamId(exam.getId());
                    response.setQuestions(questions);
                    return response;
                })
                .toList();

        if (redisTemplate != null && objectMapper != null) {
            try {
                String json = objectMapper.writeValueAsString(result);
                redisTemplate.opsForValue().set("exam:all", json, ALL_EXAMS_CACHE_TTL);
                log.info("[Redis Cache SET] exam:all -> {} đề thi, TTL 15m", result.size());
            } catch (Exception e) {
                log.warn("[Redis Cache] Lỗi lưu exam:all: {}", e.getMessage());
            }
        }

        return result;
    }

    @Override
    public Exam internalGetById(Long id) {
        return examRepository.findById(id).orElse(null);
    }

    @Override
    public List<ExamResponse> getExamsBySubjectName(String subjectName) {
        return examRepository.findBySubjectName(subjectName).stream()
                .map(examMapper::toResponse)
                .toList();
    }

    @Override
    public List<ExamResponse> getExamsByType(String type) {
        return examRepository.findByType(Enum.valueOf(ExamType.class, type)).stream()
                .map(examMapper::toResponse)
                .toList();
    }

    @Override
    public List<ExamResponse> getExamsByName(String name) {
        return examRepository.findByName(name).stream()
                .map(examMapper::toResponse)
                .toList();
    }

    @Override
    public List<ExamResponse> getExamsByCode(String code) {
        return examRepository.findByCode(code).stream()
                .map(examMapper::toResponse)
                .toList();
    }

    @Override
    public PageResponse<ExamCatalogResponse> getExamCatalog(int page, int size, String search, Long subjectId, Integer grade, String sort) {
        return getExamCatalog(page, size, search, subjectId, grade, null, sort);
    }

    @Override
    public PageResponse<ExamCatalogResponse> getExamCatalog(int page, int size, String search, Long subjectId, Integer grade, String examType, String sort) {
        String cacheKey = String.format("exam:catalog:p%d:s%d:q_%s:sub_%s:g_%s:t_%s:sort_%s",
                page, size,
                search != null ? search.trim().toLowerCase() : "",
                subjectId != null ? subjectId.toString() : "",
                grade != null ? grade.toString() : "",
                examType != null ? examType.trim().toUpperCase() : "",
                sort != null ? sort.trim() : "");

        if (redisTemplate != null && objectMapper != null) {
            try {
                String cached = redisTemplate.opsForValue().get(cacheKey);
                if (cached != null && !cached.isBlank()) {
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(PageResponse.class, ExamCatalogResponse.class);
                    PageResponse<ExamCatalogResponse> cachedResponse = objectMapper.readValue(cached, type);
                    log.info("[Redis Cache HIT] {} -> {} items", cacheKey, cachedResponse.getItems() != null ? cachedResponse.getItems().size() : 0);
                    return cachedResponse;
                }
            } catch (Exception e) {
                log.warn("[Redis Cache] Lỗi đọc {}: {}", cacheKey, e.getMessage());
            }
        }

        Sort sortOrder = Sort.by(Sort.Direction.DESC, "id");
        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            String prop = parts[0].trim();
            Sort.Direction dir = (parts.length > 1 && parts[1].trim().equalsIgnoreCase("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC;
            if (prop.equals("createdAt") || prop.equals("createdDate")) {
                sortOrder = Sort.by(dir, "createdDate");
            } else if (prop.equals("updatedAt") || prop.equals("updatedDate")) {
                sortOrder = Sort.by(dir, "updatedDate");
            } else if (prop.equals("name") || prop.equals("title")) {
                sortOrder = Sort.by(dir, "name");
            } else if (prop.equals("code")) {
                sortOrder = Sort.by(dir, "code");
            }
        }

        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), sortOrder);

        Specification<Exam> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (search != null && !search.trim().isEmpty()) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("code")), pattern)
                ));
            }
            if (subjectId != null) {
                predicates.add(cb.equal(root.get("subject").get("id"), subjectId));
            }
            if (grade != null) {
                Join<Exam, Subject> subjectJoin = root.join("subject", JoinType.LEFT);
                Join<Subject, Semester> semesterJoin = subjectJoin.join("semester", JoinType.LEFT);
                Join<Semester, Grade> gradeJoin = semesterJoin.join("grade", JoinType.LEFT);
                predicates.add(cb.equal(gradeJoin.get("grade"), grade));
            }
            if (examType != null && !examType.isBlank()) {
                String typeUpper = examType.trim().toUpperCase();
                if ("GK1".equals(typeUpper)) {
                    predicates.add(cb.or(
                            cb.like(cb.lower(root.get("name")), "%giữa k%1%"),
                            cb.like(cb.lower(root.get("code")), "%gk1%")
                    ));
                } else if ("HK1".equals(typeUpper)) {
                    predicates.add(cb.or(
                            cb.like(cb.lower(root.get("name")), "%học k%1%"),
                            cb.like(cb.lower(root.get("code")), "%ck1%"),
                            cb.like(cb.lower(root.get("code")), "%hk1%")
                    ));
                } else if ("GK2".equals(typeUpper)) {
                    predicates.add(cb.or(
                            cb.like(cb.lower(root.get("name")), "%giữa k%2%"),
                            cb.like(cb.lower(root.get("code")), "%gk2%")
                    ));
                } else if ("HK2".equals(typeUpper)) {
                    predicates.add(cb.or(
                            cb.like(cb.lower(root.get("name")), "%học k%2%"),
                            cb.like(cb.lower(root.get("code")), "%ck2%"),
                            cb.like(cb.lower(root.get("code")), "%hk2%")
                    ));
                } else if ("OTHER".equals(typeUpper)) {
                    predicates.add(cb.and(
                            cb.notLike(cb.lower(root.get("code")), "%gk1%"),
                            cb.notLike(cb.lower(root.get("code")), "%ck1%"),
                            cb.notLike(cb.lower(root.get("code")), "%hk1%"),
                            cb.notLike(cb.lower(root.get("code")), "%gk2%"),
                            cb.notLike(cb.lower(root.get("code")), "%ck2%"),
                            cb.notLike(cb.lower(root.get("code")), "%hk2%"),
                            cb.notLike(cb.lower(root.get("name")), "%giữa k%"),
                            cb.notLike(cb.lower(root.get("name")), "%học k%")
                    ));
                }
            }
            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Exam> examPage = examRepository.findAll(spec, pageable);
        PageResponse<ExamCatalogResponse> pageResponse = PageResponse.from(examPage.map(this::mapToCatalogResponse));

        if (redisTemplate != null && objectMapper != null) {
            try {
                String json = objectMapper.writeValueAsString(pageResponse);
                redisTemplate.opsForValue().set(cacheKey, json, CATALOG_CACHE_TTL);
                log.info("[Redis Cache SET] {} -> {} items, TTL 15m", cacheKey, pageResponse.getItems().size());
            } catch (Exception e) {
                log.warn("[Redis Cache] Lỗi lưu {}: {}", cacheKey, e.getMessage());
            }
        }

        return pageResponse;
    }

    @Override
    public ExamSummaryResponse getExamSummary() {
        String cacheKey = "exam:summary";
        if (redisTemplate != null && objectMapper != null) {
            try {
                String cached = redisTemplate.opsForValue().get(cacheKey);
                if (cached != null && !cached.isBlank()) {
                    ExamSummaryResponse summary = objectMapper.readValue(cached, ExamSummaryResponse.class);
                    log.info("[Redis Cache HIT] exam:summary");
                    return summary;
                }
            } catch (Exception e) {
                log.warn("[Redis Cache] Lỗi đọc exam:summary: {}", e.getMessage());
            }
        }

        List<Exam> exams = examRepository.findAll();
        long total = exams.size();
        Map<String, Long> gradeCounts = new HashMap<>();
        gradeCounts.put("6", 0L);
        gradeCounts.put("7", 0L);
        gradeCounts.put("8", 0L);
        gradeCounts.put("9", 0L);

        Map<String, Long> typeCounts = new HashMap<>();
        typeCounts.put("GK1", 0L);
        typeCounts.put("HK1", 0L);
        typeCounts.put("GK2", 0L);
        typeCounts.put("HK2", 0L);
        typeCounts.put("OTHER", 0L);

        Set<String> subjectSet = new TreeSet<>();

        for (Exam ex : exams) {
            Integer g = extractGrade(ex);
            if (g != null && gradeCounts.containsKey(String.valueOf(g))) {
                gradeCounts.put(String.valueOf(g), gradeCounts.get(String.valueOf(g)) + 1);
            }

            String t = extractExamType(ex);
            typeCounts.put(t, typeCounts.getOrDefault(t, 0L) + 1);

            String sName = ex.getSubject() != null ? ex.getSubject().getName() : ex.getSubjectName();
            if (sName != null && !sName.isBlank()) {
                String clean = sName.replaceAll("\\s*\\([^)]*\\)\\s*", " ").replaceAll("\\s*-\\s*HK\\d+", "").trim();
                if (!clean.isBlank()) subjectSet.add(clean);
            }
        }

        ExamSummaryResponse summary = ExamSummaryResponse.builder()
                .totalExams(total)
                .gradeCounts(gradeCounts)
                .typeCounts(typeCounts)
                .subjects(new ArrayList<>(subjectSet))
                .build();

        if (redisTemplate != null && objectMapper != null) {
            try {
                String json = objectMapper.writeValueAsString(summary);
                redisTemplate.opsForValue().set(cacheKey, json, SUMMARY_CACHE_TTL);
                log.info("[Redis Cache SET] exam:summary, TTL 30m");
            } catch (Exception e) {
                log.warn("[Redis Cache] Lỗi lưu exam:summary: {}", e.getMessage());
            }
        }

        return summary;
    }

    private Integer extractGrade(Exam exam) {
        try {
            if (exam.getSubject() != null && exam.getSubject().getSemester() != null && exam.getSubject().getSemester().getGrade() != null) {
                return exam.getSubject().getSemester().getGrade().getGrade();
            }
        } catch (Exception ignored) {}
        String text = ((exam.getName() != null ? exam.getName() : "") + " " +
                (exam.getSubjectName() != null ? exam.getSubjectName() : "") + " " +
                (exam.getCode() != null ? exam.getCode() : "")).toLowerCase();
        if (text.contains("khtn 6") || text.contains("khtn6") || text.contains("lớp 6") || text.contains("khoa học tự nhiên 6")) return 6;
        if (text.contains("khtn 7") || text.contains("khtn7") || text.contains("lớp 7") || text.contains("khoa học tự nhiên 7")) return 7;
        if (text.contains("khtn 8") || text.contains("khtn8") || text.contains("lớp 8") || text.contains("khoa học tự nhiên 8")) return 8;
        if (text.contains("khtn 9") || text.contains("khtn9") || text.contains("lớp 9") || text.contains("khoa học tự nhiên 9")) return 9;
        return null;
    }

    private String extractExamType(Exam exam) {
        String text = ((exam.getName() != null ? exam.getName() : "") + " " +
                (exam.getCode() != null ? exam.getCode() : "")).toLowerCase();
        if (text.contains("gk1") || text.contains("giữa kì 1") || text.contains("giữa kỳ 1")) return "GK1";
        if (text.contains("hk1") || text.contains("ck1") || text.contains("học kì 1") || text.contains("học kỳ 1")) return "HK1";
        if (text.contains("gk2") || text.contains("giữa kì 2") || text.contains("giữa kỳ 2")) return "GK2";
        if (text.contains("hk2") || text.contains("ck2") || text.contains("học kì 2") || text.contains("học kỳ 2")) return "HK2";
        return "OTHER";
    }

    private ExamCatalogResponse mapToCatalogResponse(Exam exam) {
        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamId(exam.getId());
        int questionCount = examQuestions.size();
        double totalPoints = examQuestions.stream().mapToDouble(ExamQuestion::getPoint).sum();

        ExamSubjectSummary subjectSummary = null;
        if (exam.getSubject() != null) {
            Subject s = exam.getSubject();
            Integer gradeVal = (s.getSemester() != null && s.getSemester().getGrade() != null)
                    ? s.getSemester().getGrade().getGrade() : null;
            String semesterName = (s.getSemester() != null) ? s.getSemester().getName() : null;
            subjectSummary = ExamSubjectSummary.builder()
                    .id(s.getId())
                    .name(s.getName())
                    .code(s.getCode())
                    .grade(gradeVal)
                    .semester(semesterName)
                    .build();
        } else if (exam.getSubjectName() != null) {
            subjectSummary = ExamSubjectSummary.builder()
                    .name(exam.getSubjectName())
                    .build();
        }

        ExamStatsResponse stats = ExamStatsResponse.builder()
                .questionCount(questionCount)
                .totalAssignedPoints(Math.round(totalPoints * 100.0) / 100.0)
                .participantCount(0)
                .averageScore(0.0)
                .build();

        return ExamCatalogResponse.builder()
                .id(exam.getId())
                .code(exam.getCode())
                .title(exam.getName())
                .description(exam.getDescription())
                .duration(exam.getDurationMinutes() != null ? exam.getDurationMinutes() : 45)
                .maxScore(exam.getTotalScore() != null ? exam.getTotalScore() : 10.0)
                .status(Boolean.FALSE.equals(exam.getIsActive()) ? "DRAFT" : "PUBLISHED")
                .subject(subjectSummary)
                .stats(stats)
                .createdAt(exam.getCreatedDate())
                .updatedAt(exam.getUpdatedDate())
                .build();
    }

    @Override
    public ExamResponse getExamById(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND, "Không tìm thấy đề thi với ID: " + id));
        ExamResponse response = examMapper.toResponse(exam);
        response.setQuestions(questionService.getQuestionsByExamId(id));
        return response;
    }

    @Override
    public ExamResponse updateExam(Long id, ExamUpdateRequest request) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND, "Không tìm thấy đề thi với ID: " + id));

        if (request.getCode() != null && !request.getCode().isBlank()) {
            if (examRepository.existsByCodeAndIdNot(request.getCode(), id)) {
                throw new AppException(ErrorCode.EXAM_CODE_EXISTS, "Mã đề thi '" + request.getCode() + "' đã tồn tại");
            }
            exam.setCode(request.getCode());
        }

        if (request.getName() != null) {
            exam.setName(request.getName());
        }
        if (request.getType() != null) {
            exam.setType(request.getType());
        }
        if (request.getSubjectName() != null) {
            exam.setSubjectName(request.getSubjectName());
        }
        if (request.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(request.getSubjectId())
                    .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND, "Không tìm thấy môn học với ID: " + request.getSubjectId()));
            exam.setSubject(subject);
            exam.setSubjectName(subject.getName());
        }
        if (request.getDescription() != null) {
            exam.setDescription(request.getDescription());
        }
        if (request.getDurationMinutes() != null) {
            exam.setDurationMinutes(request.getDurationMinutes());
        }
        if (request.getTotalScore() != null) {
            exam.setTotalScore(request.getTotalScore());
        }
        if (request.getIsActive() != null) {
            exam.setIsActive(request.getIsActive());
        }
        exam.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));

        exam = examRepository.save(exam);
        evictExamCache();
        ExamResponse response = examMapper.toResponse(exam);
        response.setQuestions(questionService.getQuestionsByExamId(id));
        return response;
    }

    @Override
    @Transactional
    public void deleteExam(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND, "Không tìm thấy đề thi với ID: " + id));
        examRepository.delete(exam);
        evictExamCache();
    }

    @Override
    @Transactional
    public ExamDuplicateResponse duplicateExam(Long id, ExamDuplicateRequest request) {
        Exam source = examRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND, "Không tìm thấy đề thi nguồn với ID: " + id));

        if (examRepository.existsByCode(request.getNewCode())) {
            throw new AppException(ErrorCode.EXAM_CODE_EXISTS, "Mã đề thi mới '" + request.getNewCode() + "' đã tồn tại");
        }

        Exam newExam = Exam.builder()
                .code(request.getNewCode())
                .name(request.getNewTitle())
                .type(source.getType())
                .subject(source.getSubject())
                .subjectName(source.getSubjectName())
                .description(source.getDescription())
                .durationMinutes(source.getDurationMinutes() != null ? source.getDurationMinutes() : 45)
                .totalScore(source.getTotalScore() != null ? source.getTotalScore() : 10.0)
                .isActive(source.getIsActive() != null ? source.getIsActive() : true)
                .createdDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                .updatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                .build();

        newExam = examRepository.save(newExam);

        List<ExamQuestion> sourceQuestions = examQuestionRepository.findByExamIdOrderByQuestionOrderAsc(id);
        int clonedCount = 0;
        for (ExamQuestion sq : sourceQuestions) {
            ExamQuestion newEq = ExamQuestion.builder()
                    .exam(newExam)
                    .question(sq.getQuestion())
                    .point(sq.getPoint())
                    .questionOrder(sq.getQuestionOrder())
                    .createdDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                    .updatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                    .build();
            examQuestionRepository.save(newEq);
            clonedCount++;
        }

        evictExamCache();

        return ExamDuplicateResponse.builder()
                .id(newExam.getId())
                .code(newExam.getCode())
                .title(newExam.getName())
                .clonedQuestionsCount(clonedCount)
                .build();
    }

    @Override
    public ExamBuilderResponse getExamBuilder(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND, "Không tìm thấy đề thi với ID: " + id));

        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamIdOrderByQuestionOrderAsc(id);
        double totalPoints = 0.0;
        List<ExamBuilderResponse.BuilderQuestionItem> questionItems = new ArrayList<>();

        for (ExamQuestion eq : examQuestions) {
            totalPoints += eq.getPoint();
            Question q = eq.getQuestion();
            if (q == null) continue;

            List<ExamBuilderResponse.BuilderImageItem> imageItems = Optional.ofNullable(q.getQuestionImages())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(img -> ExamBuilderResponse.BuilderImageItem.builder()
                            .id(img.getId())
                            .imageUrl(img.getUrl())
                            .caption(img.getName())
                            .imageOrder(img.getDisplayOrder())
                            .build())
                    .toList();

            List<ExamBuilderResponse.BuilderAnswerItem> answerItems = Optional.ofNullable(q.getAnswers())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(ans -> ExamBuilderResponse.BuilderAnswerItem.builder()
                            .id(ans.getId())
                            .content(ans.getContent())
                            .isCorrect(ans.isCorrect())
                            .answerOrder(ans.getId() != null ? ans.getId().intValue() : 0)
                            .explanation(ans.getExplain())
                            .images(Optional.ofNullable(ans.getAnswerImages()).orElse(Collections.emptyList()).stream()
                                    .map(aimg -> ExamBuilderResponse.BuilderImageItem.builder()
                                            .id(aimg.getId())
                                            .imageUrl(aimg.getUrl())
                                            .caption(aimg.getName())
                                            .imageOrder(aimg.getDisplayOrder())
                                            .build())
                                    .toList())
                            .build())
                    .toList();

            questionItems.add(ExamBuilderResponse.BuilderQuestionItem.builder()
                    .examQuestionId(eq.getId())
                    .questionId(q.getId())
                    .questionOrder(eq.getQuestionOrder())
                    .point(eq.getPoint())
                    .content(q.getContent())
                    .type(q.getType())
                    .difficultyLevel("MEDIUM")
                    .topic(q.getDescription())
                    .explanation(q.getExplain())
                    .images(imageItems)
                    .answers(answerItems)
                    .build());
        }

        double maxScore = exam.getTotalScore() != null ? exam.getTotalScore() : 10.0;
        boolean isValidTotalPoints = Math.abs(totalPoints - maxScore) < 0.001;

        return ExamBuilderResponse.builder()
                .exam(ExamBuilderResponse.ExamBasicInfo.builder()
                        .id(exam.getId())
                        .code(exam.getCode())
                        .title(exam.getName())
                        .duration(exam.getDurationMinutes() != null ? exam.getDurationMinutes() : 45)
                        .maxScore(maxScore)
                        .subjectName(exam.getSubjectName())
                        .status(Boolean.FALSE.equals(exam.getIsActive()) ? "DRAFT" : "PUBLISHED")
                        .description(exam.getDescription())
                        .build())
                .summary(ExamBuilderResponse.ExamSummaryInfo.builder()
                        .totalQuestions(examQuestions.size())
                        .totalPoints(Math.round(totalPoints * 100.0) / 100.0)
                        .isValidTotalPoints(isValidTotalPoints)
                        .build())
                .questions(questionItems)
                .build();
    }

    @Override
    @Transactional
    public ExamReorderResponse reorderQuestions(Long id, ExamQuestionsReorderRequest request) {
        if (!examRepository.existsById(id)) {
            throw new AppException(ErrorCode.EXAM_NOT_FOUND, "Không tìm thấy đề thi với ID: " + id);
        }

        double totalPoints = 0.0;
        int count = 0;

        for (ExamQuestionsReorderRequest.ReorderItem item : request.getItems()) {
            ExamQuestion eq = examQuestionRepository.findById(item.getExamQuestionId())
                    .orElseThrow(() -> new AppException(ErrorCode.EXAM_QUESTION_NOT_FOUND,
                            "Không tìm thấy liên kết câu hỏi - đề thi ID: " + item.getExamQuestionId()));
            eq.setQuestionOrder(item.getNewOrder());
            eq.setPoint(item.getPoint());
            eq.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            examQuestionRepository.save(eq);
            totalPoints += item.getPoint();
            count++;
        }

        evictExamCache();

        return ExamReorderResponse.builder()
                .totalQuestions(count)
                .totalPoints(Math.round(totalPoints * 100.0) / 100.0)
                .build();
    }

    @Override
    @Transactional
    public CompositeQuestionResponse addCompositeQuestion(Long id, CompositeQuestionRequest request) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND, "Không tìm thấy đề thi với ID: " + id));

        Question question = Question.builder()
                .content(request.getContent())
                .type(request.getType() != null ? request.getType() : QuestionType.SINGLE_CHOICE)
                .explain(request.getExplanation())
                .description(request.getTopic())
                .createdDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                .updatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                .build();
        question = questionRepository.save(question);

        List<Answer> answers = new ArrayList<>();
        if (request.getAnswers() != null) {
            for (CompositeQuestionRequest.CompositeAnswerRequest ar : request.getAnswers()) {
                Answer a = Answer.builder()
                        .question(question)
                        .content(ar.getContent())
                        .isCorrect(ar.isCorrect())
                        .explain(ar.getExplanation())
                        .type(AnswerType.TEXT)
                        .createdDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                        .updatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                        .build();
                answers.add(answerRepository.save(a));
            }
        }
        question.setAnswers(answers);

        if (request.getImages() != null) {
            int imgOrder = 1;
            List<QuestionImage> questionImages = new ArrayList<>();
            for (CompositeQuestionRequest.CompositeImageRequest ir : request.getImages()) {
                QuestionImage qi = QuestionImage.builder()
                        .question(question)
                        .url(ir.getImageUrl())
                        .name(ir.getCaption())
                        .displayOrder(imgOrder++)
                        .createdDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                        .build();
                questionImages.add(questionImageRepository.save(qi));
            }
            question.setQuestionImages(questionImages);
        }

        long count = examQuestionRepository.countByExamId(id);
        int nextOrder = (int) count + 1;

        ExamQuestion eq = ExamQuestion.builder()
                .exam(exam)
                .question(question)
                .point(request.getPoint() > 0 ? request.getPoint() : 0.25)
                .questionOrder(nextOrder)
                .createdDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                .updatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                .build();
        eq = examQuestionRepository.save(eq);

        evictExamCache();

        return CompositeQuestionResponse.builder()
                .examQuestionId(eq.getId())
                .questionId(question.getId())
                .questionOrder(eq.getQuestionOrder())
                .point(eq.getPoint())
                .content(question.getContent())
                .answersCount(answers.size())
                .build();
    }

    @Override
    @Transactional
    public void removeQuestionFromExam(Long examId, Long questionId) {
        if (!examRepository.existsById(examId)) {
            throw new AppException(ErrorCode.EXAM_NOT_FOUND, "Không tìm thấy đề thi với ID: " + examId);
        }
        ExamQuestion eq = examQuestionRepository.findFirstByExamIdAndQuestionId(examId, questionId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_QUESTION_NOT_FOUND,
                        "Không tìm thấy câu hỏi ID " + questionId + " trong đề thi ID " + examId));
        examQuestionRepository.delete(eq);
        evictExamCache();
    }

    @Override
    @Transactional
    public List<ExamQuestionResponse> pickQuestionsFromBank(Long examId, PickFromBankRequest request) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND, "Không tìm thấy đề thi với ID: " + examId));

        int currentOrder = (int) examQuestionRepository.countByExamId(examId);
        List<ExamQuestionResponse> responses = new ArrayList<>();

        for (Long qId : request.getQuestionIds()) {
            Question q = questionRepository.findById(qId).orElse(null);
            if (q == null) continue;

            Optional<ExamQuestion> existing = examQuestionRepository.findFirstByExamIdAndQuestionId(examId, qId);
            if (existing.isPresent()) continue;

            currentOrder++;
            ExamQuestion eq = ExamQuestion.builder()
                    .exam(exam)
                    .question(q)
                    .point(request.getDefaultPoint())
                    .questionOrder(currentOrder)
                    .createdDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                    .updatedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                    .build();
            eq = examQuestionRepository.save(eq);
            responses.add(examQuestionMapper.toResponse(eq));
        }

        evictExamCache();

        return responses;
    }
}
