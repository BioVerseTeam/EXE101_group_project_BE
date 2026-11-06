package com.example.exe101_bioverse.feedback.service.impl;

import com.example.exe101_bioverse.auth.entity.User;
import com.example.exe101_bioverse.auth.repository.UserRepository;
import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import com.example.exe101_bioverse.common.response.PageResponse;
import com.example.exe101_bioverse.feedback.dto.request.CreateFeedbackRequest;
import com.example.exe101_bioverse.feedback.dto.request.UpdateFeedbackStatusRequest;
import com.example.exe101_bioverse.feedback.dto.response.FeedbackResponse;
import com.example.exe101_bioverse.feedback.entity.Feedback;
import com.example.exe101_bioverse.feedback.enums.FeedbackCategory;
import com.example.exe101_bioverse.feedback.enums.FeedbackStatus;
import com.example.exe101_bioverse.feedback.repository.FeedbackRepository;
import com.example.exe101_bioverse.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public FeedbackResponse createFeedback(CreateFeedbackRequest request, Long currentUserId) {
        User user = null;
        String userName = request.getUserName();
        String userEmail = request.getUserEmail();

        if (currentUserId != null) {
            user = userRepository.findById(currentUserId).orElse(null);
            if (user != null) {
                if (userEmail == null || userEmail.isBlank()) {
                    userEmail = user.getEmail();
                }
                if (userName == null || userName.isBlank()) {
                    userName = user.getFullName();
                }
            }
        }

        Feedback feedback = Feedback.builder()
                .user(user)
                .userName(userName != null && !userName.isBlank() ? userName.trim() : "Khách ẩn danh")
                .userEmail(userEmail != null && !userEmail.isBlank() ? userEmail.trim() : null)
                .category(request.getCategory() != null ? request.getCategory() : FeedbackCategory.GENERAL)
                .rating(request.getRating() != null ? request.getRating() : 5)
                .title(request.getTitle() != null ? request.getTitle().trim() : null)
                .content(request.getContent().trim())
                .pageUrl(request.getPageUrl())
                .deviceInfo(request.getDeviceInfo())
                .status(FeedbackStatus.PENDING)
                .build();

        Feedback saved = feedbackRepository.save(feedback);
        log.info("New feedback created with ID: {}, category: {}, rating: {}", saved.getId(), saved.getCategory(), saved.getRating());
        return FeedbackResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FeedbackResponse> searchFeedbacks(
            FeedbackStatus status,
            FeedbackCategory category,
            Integer rating,
            String keyword,
            Pageable pageable) {

        String pattern = (keyword != null && !keyword.isBlank())
                ? "%" + keyword.trim().toLowerCase() + "%"
                : null;

        Page<Feedback> page = feedbackRepository.search(status, category, rating, pattern, pageable);
        return PageResponse.from(page.map(FeedbackResponse::from));
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackResponse getFeedback(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_FOUND));
        return FeedbackResponse.from(feedback);
    }

    @Override
    @Transactional
    public FeedbackResponse updateStatus(Long id, UpdateFeedbackStatusRequest request) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_FOUND));

        feedback.setStatus(request.getStatus());
        if (request.getAdminNotes() != null) {
            feedback.setAdminNotes(request.getAdminNotes().trim());
        }

        if (request.getStatus() == FeedbackStatus.RESOLVED || request.getStatus() == FeedbackStatus.REVIEWED) {
            if (feedback.getRespondedAt() == null) {
                feedback.setRespondedAt(LocalDateTime.now());
            }
        }

        Feedback updated = feedbackRepository.save(feedback);
        log.info("Feedback ID: {} status updated to: {}", updated.getId(), updated.getStatus());
        return FeedbackResponse.from(updated);
    }

    @Override
    @Transactional
    public void deleteFeedback(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_FOUND));
        feedbackRepository.delete(feedback);
        log.info("Feedback ID: {} deleted", id);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportToCsv(
            FeedbackStatus status,
            FeedbackCategory category,
            Integer rating,
            String keyword) {

        String pattern = (keyword != null && !keyword.isBlank())
                ? "%" + keyword.trim().toLowerCase() + "%"
                : null;

        List<Feedback> items = feedbackRepository.findAllMatching(status, category, rating, pattern);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        // UTF-8 BOM for Microsoft Excel auto-detecting Unicode/Vietnamese
        sb.append('\uFEFF');

        // CSV Header
        sb.append("Mã ID,Ngày gửi,Người gửi,Email,Loại góp ý,Đánh giá (Sao),Tiêu đề,Nội dung chi tiết,Đường dẫn trang,Thiết bị & Trình duyệt,Trạng thái,Ghi chú BQT,Thời gian phản hồi\n");

        for (Feedback f : items) {
            String id = String.valueOf(f.getId());
            String createdAt = f.getCreatedAt() != null ? f.getCreatedAt().format(dtf) : "";
            String sender = f.getUser() != null ? f.getUser().getFullName() : (f.getUserName() != null ? f.getUserName() : "Khách ẩn danh");
            String email = f.getUser() != null ? f.getUser().getEmail() : (f.getUserEmail() != null ? f.getUserEmail() : "");
            String cat = f.getCategory() != null ? f.getCategory().getDisplayName() : "";
            String star = f.getRating() != null ? f.getRating() + " sao" : "";
            String title = f.getTitle() != null ? f.getTitle() : "";
            String content = f.getContent() != null ? f.getContent() : "";
            String pageUrl = f.getPageUrl() != null ? f.getPageUrl() : "";
            String deviceInfo = f.getDeviceInfo() != null ? f.getDeviceInfo() : "";
            String st = f.getStatus() != null ? f.getStatus().getDisplayName() : "";
            String notes = f.getAdminNotes() != null ? f.getAdminNotes() : "";
            String respondedAt = f.getRespondedAt() != null ? f.getRespondedAt().format(dtf) : "";

            sb.append(escapeCsv(id)).append(",")
              .append(escapeCsv(createdAt)).append(",")
              .append(escapeCsv(sender)).append(",")
              .append(escapeCsv(email)).append(",")
              .append(escapeCsv(cat)).append(",")
              .append(escapeCsv(star)).append(",")
              .append(escapeCsv(title)).append(",")
              .append(escapeCsv(content)).append(",")
              .append(escapeCsv(pageUrl)).append(",")
              .append(escapeCsv(deviceInfo)).append(",")
              .append(escapeCsv(st)).append(",")
              .append(escapeCsv(notes)).append(",")
              .append(escapeCsv(respondedAt)).append("\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escapeCsv(String val) {
        if (val == null) return "\"\"";
        String s = val.replace("\"", "\"\"");
        return "\"" + s + "\"";
    }
}
