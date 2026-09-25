package com.example.exe101_bioverse.exam.service.impl;

import com.example.exe101_bioverse.auth.entity.User;
import com.example.exe101_bioverse.auth.repository.UserRepository;
import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.exam.dto.request.StudentExamSubmitRequest;
import com.example.exe101_bioverse.exam.dto.request.StudentSubmittedAnswerRequest;
import com.example.exe101_bioverse.exam.dto.response.*;
import com.example.exe101_bioverse.exam.entity.*;
import com.example.exe101_bioverse.exam.repository.*;
import com.example.exe101_bioverse.exam.service.StudentExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentExamServiceImpl implements StudentExamService {

    private final ExamRepository examRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final AttemptAnswerRepository attemptAnswerRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public StudentExamPaperResponse getAntiCheatExamPaper(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));

        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamIdOrderByQuestionOrderAsc(examId);

        List<StudentQuestionPaperResponse> questionResponses = examQuestions.stream()
                .map(eq -> {
                    Question q = eq.getQuestion();
                    List<StudentAnswerPaperResponse> answerResponses = (q.getAnswers() != null)
                            ? q.getAnswers().stream()
                            .map(a -> StudentAnswerPaperResponse.builder()
                                    .id(a.getId())
                                    .type(a.getType())
                                    .content(a.getContent())
                                    .images(mapAnswerImages(a.getAnswerImages()))
                                    .build())
                            .collect(Collectors.toList())
                            : Collections.<StudentAnswerPaperResponse>emptyList();

                    return StudentQuestionPaperResponse.builder()
                            .id(q.getId())
                            .questionOrder(eq.getQuestionOrder())
                            .type(q.getType())
                            .point(eq.getPoint())
                            .content(q.getContent())
                            .modelAssetId(null)
                            .images(mapQuestionImages(q.getQuestionImages()))
                            .answers(answerResponses)
                            .build();
                })
                .collect(Collectors.toList());

        return StudentExamPaperResponse.builder()
                .examId(exam.getId())
                .code(exam.getCode())
                .name(exam.getName())
                .subjectName(exam.getSubjectName())
                .description(exam.getDescription())
                .durationMinutes(exam.getDurationMinutes())
                .totalScore(exam.getTotalScore())
                .totalQuestions(examQuestions.size())
                .questions(questionResponses)
                .build();
    }

    @Override
    @Transactional
    public StudentExamSubmitResponse submitExam(Long examId, Long userId, StudentExamSubmitRequest request) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));

        User user;
        if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        } else {
            user = userRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new AppException(ErrorCode.MISSING_ACCESS_TOKEN));
        }

        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamIdOrderByQuestionOrderAsc(examId);

        Map<Long, StudentSubmittedAnswerRequest> submittedMap = (request.getAnswers() != null)
                ? request.getAnswers().stream()
                .filter(a -> a.getQuestionId() != null)
                .collect(Collectors.toMap(
                        StudentSubmittedAnswerRequest::getQuestionId,
                        Function.identity(),
                        (existing, replacement) -> replacement))
                : Collections.emptyMap();

        int totalQuestions = examQuestions.size();
        int correctCount = 0;
        double totalExamPoints = examQuestions.stream()
                .mapToDouble(ExamQuestion::getPoint)
                .sum();
        double earnedPoints = 0.0;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startedAt = request.getStartedAt() != null
                ? request.getStartedAt()
                : now.minusSeconds(request.getTimeSpentSec() != null ? request.getTimeSpentSec() : 0);

        ExamAttempt attempt = ExamAttempt.builder()
                .user(user)
                .exam(exam)
                .totalQuestions(totalQuestions)
                .startedAt(startedAt)
                .submittedAt(now)
                .timeSpentSec(request.getTimeSpentSec())
                .createdAt(now)
                .build();

        List<AttemptAnswer> attemptAnswers = new ArrayList<>();

        for (ExamQuestion eq : examQuestions) {
            Question q = eq.getQuestion();
            StudentSubmittedAnswerRequest sub = submittedMap.get(q.getId());
            Long selectedAnswerId = sub != null ? sub.getSelectedAnswerId() : null;
            Integer qTimeSpent = sub != null ? sub.getTimeSpentSec() : null;

            Answer selectedAnswer = null;
            boolean isCorrect = false;

            if (selectedAnswerId != null && q.getAnswers() != null) {
                selectedAnswer = q.getAnswers().stream()
                        .filter(a -> a.getId().equals(selectedAnswerId))
                        .findFirst()
                        .orElse(null);

                if (selectedAnswer != null && selectedAnswer.isCorrect()) {
                    isCorrect = true;
                    correctCount++;
                    double qPoint = eq.getPoint() > 0
                            ? eq.getPoint()
                            : (totalQuestions > 0 ? 10.0 / totalQuestions : 0.0);
                    earnedPoints += qPoint;
                }
            }

            AttemptAnswer aa = AttemptAnswer.builder()
                    .attempt(attempt)
                    .question(q)
                    .selectedAnswer(selectedAnswer)
                    .isCorrect(isCorrect)
                    .timeSpentSec(qTimeSpent)
                    .build();

            attemptAnswers.add(aa);
        }

        // Tính điểm chính thức trên thang 10
        double finalScore = 0.0;
        if (totalExamPoints > 0) {
            finalScore = (earnedPoints / totalExamPoints) * 10.0;
        } else if (totalQuestions > 0) {
            finalScore = (correctCount * 10.0) / totalQuestions;
        }
        finalScore = Math.min(10.0, Math.round(finalScore * 100.0) / 100.0);

        // Tính XP
        int earnedXp = (int) Math.round(finalScore * 10);
        if (totalQuestions > 0 && correctCount == totalQuestions) {
            earnedXp += 20; // Thưởng điểm tuyệt đối
        }

        attempt.setScore(finalScore);
        attempt.setCorrectCount(correctCount);
        attempt.setAttemptAnswers(attemptAnswers);

        ExamAttempt savedAttempt = examAttemptRepository.save(attempt);
        attemptAnswerRepository.saveAll(attemptAnswers);

        String feedback = generateFeedback(finalScore);

        return StudentExamSubmitResponse.builder()
                .attemptId(savedAttempt.getId())
                .examId(exam.getId())
                .examTitle(exam.getName())
                .score(finalScore)
                .totalQuestions(totalQuestions)
                .correctCount(correctCount)
                .timeSpentSec(request.getTimeSpentSec())
                .earnedXp(earnedXp)
                .submittedAt(now)
                .feedback(feedback)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public StudentExamAttemptDetailResponse getExamAttemptDetail(Long attemptId, Long userId) {
        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_ATTEMPT_NOT_FOUND));

        if (userId != null && !attempt.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        Exam exam = attempt.getExam();
        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamIdOrderByQuestionOrderAsc(exam.getId());
        List<AttemptAnswer> attemptAnswers = attemptAnswerRepository.findByAttemptId(attemptId);

        Map<Long, AttemptAnswer> answerMap = attemptAnswers.stream()
                .collect(Collectors.toMap(
                        aa -> aa.getQuestion().getId(),
                        Function.identity(),
                        (existing, replacement) -> replacement));

        List<StudentAttemptQuestionDetailResponse> questionDetails = examQuestions.stream()
                .map(eq -> {
                    Question q = eq.getQuestion();
                    AttemptAnswer aa = answerMap.get(q.getId());

                    Long selectedAnswerId = (aa != null && aa.getSelectedAnswer() != null)
                            ? aa.getSelectedAnswer().getId()
                            : null;
                    Boolean isCorrect = aa != null ? aa.getIsCorrect() : false;
                    Double point = eq.getPoint();
                    Double earnedPoint = Boolean.TRUE.equals(isCorrect) ? point : 0.0;

                    List<StudentAttemptAnswerDetailResponse> answers = (q.getAnswers() != null)
                            ? q.getAnswers().stream()
                            .map(a -> StudentAttemptAnswerDetailResponse.builder()
                                    .id(a.getId())
                                    .type(a.getType())
                                    .content(a.getContent())
                                    .isCorrect(a.isCorrect())
                                    .isSelected(a.getId().equals(selectedAnswerId))
                                    .images(mapAnswerImages(a.getAnswerImages()))
                                    .build())
                            .collect(Collectors.toList())
                            : Collections.<StudentAttemptAnswerDetailResponse>emptyList();

                    return StudentAttemptQuestionDetailResponse.builder()
                            .questionId(q.getId())
                            .questionOrder(eq.getQuestionOrder())
                            .content(q.getContent())
                            .type(q.getType())
                            .point(point)
                            .earnedPoint(earnedPoint)
                            .explanation(q.getExplain())
                            .selectedAnswerId(selectedAnswerId)
                            .isCorrect(isCorrect)
                            .modelAssetId(null)
                            .images(mapQuestionImages(q.getQuestionImages()))
                            .answers(answers)
                            .build();
                })
                .collect(Collectors.toList());

        return StudentExamAttemptDetailResponse.builder()
                .attemptId(attempt.getId())
                .examId(exam.getId())
                .examCode(exam.getCode())
                .examTitle(exam.getName())
                .score(attempt.getScore())
                .totalQuestions(attempt.getTotalQuestions())
                .correctCount(attempt.getCorrectCount())
                .timeSpentSec(attempt.getTimeSpentSec())
                .startedAt(attempt.getStartedAt())
                .submittedAt(attempt.getSubmittedAt())
                .questions(questionDetails)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public StudentExamHistoryResponse getStudentHistory(Long userId) {
        if (userId == null) {
            User firstUser = userRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new AppException(ErrorCode.MISSING_ACCESS_TOKEN));
            userId = firstUser.getId();
        } else {
            if (!userRepository.existsById(userId)) {
                throw new AppException(ErrorCode.USER_NOT_FOUND);
            }
        }

        List<ExamAttempt> attempts = examAttemptRepository.findByUserIdOrderBySubmittedAtDesc(userId);

        int totalExams = attempts.size();
        double avgScore = attempts.isEmpty() ? 0.0
                : Math.round(attempts.stream()
                .mapToDouble(a -> a.getScore() != null ? a.getScore() : 0.0)
                .average()
                .orElse(0.0) * 100.0) / 100.0;

        double highestScore = attempts.stream()
                .mapToDouble(a -> a.getScore() != null ? a.getScore() : 0.0)
                .max()
                .orElse(0.0);

        long totalTimeSpent = attempts.stream()
                .mapToLong(a -> a.getTimeSpentSec() != null ? a.getTimeSpentSec() : 0L)
                .sum();

        int totalCorrect = attempts.stream()
                .mapToInt(a -> a.getCorrectCount() != null ? a.getCorrectCount() : 0)
                .sum();

        StudentHistorySummaryResponse summary = StudentHistorySummaryResponse.builder()
                .totalExamsTaken(totalExams)
                .averageScore(avgScore)
                .highestScore(highestScore)
                .totalTimeSpentSec(totalTimeSpent)
                .totalCorrectQuestions(totalCorrect)
                .build();

        List<StudentAttemptSummaryResponse> attemptSummaries = attempts.stream()
                .map(a -> {
                    Exam exam = a.getExam();
                    return StudentAttemptSummaryResponse.builder()
                            .attemptId(a.getId())
                            .examId(exam.getId())
                            .examCode(exam.getCode())
                            .examTitle(exam.getName())
                            .subjectName(exam.getSubjectName())
                            .score(a.getScore())
                            .totalQuestions(a.getTotalQuestions())
                            .correctCount(a.getCorrectCount())
                            .timeSpentSec(a.getTimeSpentSec())
                            .submittedAt(a.getSubmittedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return StudentExamHistoryResponse.builder()
                .summary(summary)
                .attempts(attemptSummaries)
                .build();
    }

    private String generateFeedback(double score) {
        if (score >= 9.0) {
            return "Xuất sắc! Bạn nắm rất vững kiến thức và hoàn thành bài thi một cách hoàn hảo!";
        } else if (score >= 8.0) {
            return "Giỏi lắm! Bạn đã đạt kết quả rất tốt, hãy tiếp tục phát huy phong độ nhé!";
        } else if (score >= 6.5) {
            return "Khá tốt! Bạn đã vượt qua bài thi. Hãy xem lại các câu trả lời sai để củng cố kiến thức.";
        } else if (score >= 5.0) {
            return "Đạt yêu cầu! Hãy ôn luyện thêm các phần kiến thức còn yếu để nâng cao điểm số.";
        } else {
            return "Cần cố gắng thêm! Hãy xem kỹ lời giải chi tiết và làm lại bài thi để cải thiện kết quả.";
        }
    }

    private List<QuestionImageResponse> mapQuestionImages(List<QuestionImage> images) {
        if (images == null) return Collections.emptyList();
        return images.stream()
                .map(img -> QuestionImageResponse.builder()
                        .id(img.getId())
                        .name(img.getName())
                        .displayOrder(img.getDisplayOrder())
                        .createdDate(img.getCreatedDate())
                        .url(img.getUrl())
                        .questionId(img.getQuestion() != null ? img.getQuestion().getId() : null)
                        .build())
                .collect(Collectors.toList());
    }

    private List<AnswerImageResponse> mapAnswerImages(List<AnswerImage> images) {
        if (images == null) return Collections.emptyList();
        return images.stream()
                .map(img -> AnswerImageResponse.builder()
                        .id(img.getId())
                        .name(img.getName())
                        .displayOrder(img.getDisplayOrder())
                        .createdDate(img.getCreatedDate())
                        .url(img.getUrl())
                        .answerId(img.getAnswer() != null ? img.getAnswer().getId() : null)
                        .build())
                .collect(Collectors.toList());
    }
}
