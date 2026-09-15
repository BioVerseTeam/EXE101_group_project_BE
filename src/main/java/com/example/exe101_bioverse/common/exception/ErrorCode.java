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

    // Request 14xx
    INVALID_DATA(1400, "Dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED(1403, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    INVALID_FILE(1410, "File không hợp lệ", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(1411, "File vượt quá dung lượng cho phép", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
