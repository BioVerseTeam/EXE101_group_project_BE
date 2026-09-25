package com.example.exe101_bioverse.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    SUCCESS(1000, "Thành công", HttpStatus.OK),
    UNCATEGORIZED(9999, "Lỗi hệ thống không xác định", HttpStatus.INTERNAL_SERVER_ERROR),

    // Auth / User 1xxx
    USER_NOT_FOUND(1001, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS(1002, "Email đã được đăng ký", HttpStatus.CONFLICT),
    PHONE_ALREADY_EXISTS(1003, "Số điện thoại đã được đăng ký", HttpStatus.CONFLICT),
    INVALID_CREDENTIALS(1004, "Email hoặc mật khẩu không đúng", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN(1005, "Refresh token không hợp lệ hoặc đã hết hạn", HttpStatus.UNAUTHORIZED),
    ACCOUNT_NOT_ACTIVE(1006, "Tài khoản không hoạt động", HttpStatus.FORBIDDEN),
    INVALID_ACCESS_TOKEN(1007, "Access token không hợp lệ", HttpStatus.UNAUTHORIZED),
    MISSING_ACCESS_TOKEN(1008, "Thiếu access token", HttpStatus.UNAUTHORIZED),
    ROLE_NOT_CONFIGURED(1009, "Vai trò chưa được cấu hình", HttpStatus.INTERNAL_SERVER_ERROR),
    OTP_INVALID(1010, "Mã OTP không đúng", HttpStatus.BAD_REQUEST),
    OTP_EXPIRED(1011, "Mã OTP đã hết hạn hoặc chưa được gửi", HttpStatus.BAD_REQUEST),
    OTP_RESEND_TOO_SOON(1012, "Vui lòng đợi trước khi gửi lại mã OTP", HttpStatus.TOO_MANY_REQUESTS),
    OTP_MAX_ATTEMPTS(1013, "Bạn đã nhập sai quá số lần cho phép. Vui lòng gửi lại mã OTP", HttpStatus.TOO_MANY_REQUESTS),
    PENDING_REGISTRATION_EXPIRED(1014, "Phiên đăng ký đã hết hạn. Vui lòng đăng ký lại", HttpStatus.BAD_REQUEST),
    INVALID_RESET_TOKEN(1015, "Phiên đặt lại mật khẩu không hợp lệ hoặc đã hết hạn", HttpStatus.UNAUTHORIZED),
    EMAIL_SEND_FAILED(1016, "Không gửi được email. Vui lòng thử lại sau", HttpStatus.INTERNAL_SERVER_ERROR),
    CANNOT_MODIFY_OWN_ACCOUNT(1017, "Không thể thay đổi vai trò hoặc trạng thái của chính mình", HttpStatus.FORBIDDEN),
    LAST_ACTIVE_ADMIN(1018, "Phải còn ít nhất một admin đang hoạt động", HttpStatus.CONFLICT),
    ROLE_NOT_FOUND(1019, "Không tìm thấy vai trò", HttpStatus.NOT_FOUND),
    ROLE_CODE_ALREADY_EXISTS(1020, "Mã vai trò đã tồn tại", HttpStatus.CONFLICT),
    ROLE_IN_USE(1021, "Không thể xóa vai trò đang được gán cho người dùng", HttpStatus.CONFLICT),
    SYSTEM_ROLE_PROTECTED(1022, "Không thể xóa hoặc đổi mã vai trò hệ thống", HttpStatus.FORBIDDEN),

    // Request 14xx
    INVALID_DATA(1400, "Dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED(1403, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    INVALID_FILE(1410, "File không hợp lệ", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(1411, "File vượt quá dung lượng cho phép", HttpStatus.BAD_REQUEST),

    // Storage / 3D models 15xx
    MODEL_NOT_FOUND(1501, "Không tìm thấy model 3D", HttpStatus.NOT_FOUND),
    STORAGE_NOT_CONFIGURED(1502, "Chưa cấu hình Cloudflare R2", HttpStatus.SERVICE_UNAVAILABLE),
    STORAGE_ERROR(1503, "Không tải được model 3D", HttpStatus.BAD_GATEWAY),
    CATEGORY_NOT_FOUND(1504, "Không tìm thấy loại mẫu", HttpStatus.NOT_FOUND),
    CATEGORY_NAME_EXISTS(1505, "Tên loại mẫu đã tồn tại", HttpStatus.CONFLICT),
    LAB_NOT_FOUND(1506, "Không tìm thấy lab", HttpStatus.NOT_FOUND),
    LAB_CODE_EXISTS(1507, "Mã lab đã tồn tại", HttpStatus.CONFLICT),
    SYSTEM_LAB_PROTECTED(1508, "Không thể xóa hoặc đổi mã lab hệ thống", HttpStatus.FORBIDDEN),
    REACTION_NOT_FOUND(1509, "Không tìm thấy phương trình hoá học", HttpStatus.NOT_FOUND),
    REACTION_CODE_EXISTS(1510, "Mã phương trình đã tồn tại", HttpStatus.CONFLICT),
    SYSTEM_REACTION_PROTECTED(1511, "Không thể ẩn hoặc đổi mã phương trình hệ thống", HttpStatus.FORBIDDEN),

    // Exam / Education 16xx
    CLASS_NOT_FOUND(1601, "Không tìm thấy khối lớp", HttpStatus.NOT_FOUND),
    CLASS_GRADE_EXISTS(1602, "Khối lớp đã tồn tại trong hệ thống", HttpStatus.CONFLICT),
    SEMESTER_NOT_FOUND(1603, "Không tìm thấy học kỳ", HttpStatus.NOT_FOUND),
    SUBJECT_NOT_FOUND(1604, "Không tìm thấy môn học", HttpStatus.NOT_FOUND),
    EXAM_NOT_FOUND(1605, "Không tìm thấy đề thi", HttpStatus.NOT_FOUND),
    QUESTION_NOT_FOUND(1606, "Không tìm thấy câu hỏi", HttpStatus.NOT_FOUND),
    ANSWER_NOT_FOUND(1607, "Không tìm thấy đáp án", HttpStatus.NOT_FOUND),
    EXAM_QUESTION_NOT_FOUND(1608, "Không tìm thấy câu hỏi trong đề thi", HttpStatus.NOT_FOUND),
    QUESTION_IMAGE_NOT_FOUND(1609, "Không tìm thấy hình ảnh câu hỏi", HttpStatus.NOT_FOUND),
    ANSWER_IMAGE_NOT_FOUND(1610, "Không tìm thấy hình ảnh đáp án", HttpStatus.NOT_FOUND),
    UNSUPPORTED_RETURN_TYPE(1611, "Kiểu dữ liệu phản hồi không được hỗ trợ", HttpStatus.BAD_REQUEST),
    EXAM_CODE_EXISTS(1612, "Mã đề thi đã tồn tại trong hệ thống", HttpStatus.CONFLICT),
    EXAM_ATTEMPT_NOT_FOUND(1613, "Lượt làm bài thi không tồn tại trong hệ thống", HttpStatus.NOT_FOUND);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
