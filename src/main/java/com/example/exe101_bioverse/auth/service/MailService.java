package com.example.exe101_bioverse.auth.service;

import com.example.exe101_bioverse.auth.enums.OtpPurpose;
import com.example.exe101_bioverse.common.exception.AppException;
import com.example.exe101_bioverse.common.exception.ErrorCode;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.Locale;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine emailTemplateEngine;
    private final String from;
    private final String fromName;
    private final String appUrl;

    public MailService(
            JavaMailSender mailSender,
            @Qualifier("emailTemplateEngine") SpringTemplateEngine emailTemplateEngine,
            @Value("${bioverse.mail.from:}") String from,
            @Value("${bioverse.mail.from-name:BioVerse}") String fromName,
            @Value("${bioverse.mail.app-url:https://bioverse.eraidev.id.vn}") String appUrl
    ) {
        this.mailSender = mailSender;
        this.emailTemplateEngine = emailTemplateEngine;
        this.from = from;
        this.fromName = fromName;
        this.appUrl = trimTrailingSlash(appUrl);
    }

    public void sendOtp(String to, String fullName, String otp, OtpPurpose purpose, long expireMinutes) {
        if (from == null || from.isBlank()) {
            throw new AppException(ErrorCode.EMAIL_SEND_FAILED, "Chưa cấu hình MAIL_FROM (Brevo verified sender)");
        }

        Context context = new Context(Locale.forLanguageTag("vi"));
        context.setVariable("otp", otp);
        context.setVariable("fullName", displayName(fullName));
        context.setVariable("expireMinutes", expireMinutes);
        context.setVariable("year", Year.now().getValue());
        context.setVariable("purpose", purpose.name());
        context.setVariable("purposeTitle", purposeTitle(purpose));
        context.setVariable("purposeHint", purposeHint(purpose));
        context.setVariable("purposeBadge", purposeBadge(purpose));
        context.setVariable("ctaLabel", purposeCta(purpose));
        context.setVariable("appUrl", appUrl);
        context.setVariable("otpPageUrl", appUrl + "/pages/otp.html");

        String html = emailTemplateEngine.process("mail/otp", context);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setSubject(purposeSubject(purpose));
            helper.setFrom(new InternetAddress(from, fromName, StandardCharsets.UTF_8.name()));
            helper.setText(html, true);
            mailSender.send(message);
            log.info("OTP email sent via Brevo to {} for {}", to, purpose);
        } catch (Exception ex) {
            log.error("Failed to send OTP email to {} for {}", to, purpose, ex);
            throw new AppException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    private String displayName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return "bạn";
        }
        return fullName.trim();
    }

    private String purposeSubject(OtpPurpose purpose) {
        return switch (purpose) {
            case REGISTER -> "BioVerse · Mã xác thực đăng ký";
            case RESET_PASSWORD -> "BioVerse · Mã đặt lại mật khẩu";
            case CHANGE_PASSWORD -> "BioVerse · Mã xác thực đổi mật khẩu";
        };
    }

    private String purposeTitle(OtpPurpose purpose) {
        return switch (purpose) {
            case REGISTER -> "Xác thực đăng ký tài khoản";
            case RESET_PASSWORD -> "Đặt lại mật khẩu";
            case CHANGE_PASSWORD -> "Đổi mật khẩu";
        };
    }

    private String purposeHint(OtpPurpose purpose) {
        return switch (purpose) {
            case REGISTER -> "Nhập mã 6 số này trên trang OTP để hoàn tất đăng ký sổ tay BioVerse.";
            case RESET_PASSWORD -> "Nhập mã 6 số này để xác thực và đặt lại mật khẩu tài khoản BioVerse.";
            case CHANGE_PASSWORD -> "Nhập mã 6 số này cùng mật khẩu mới để đổi mật khẩu tài khoản BioVerse.";
        };
    }

    private String purposeBadge(OtpPurpose purpose) {
        return switch (purpose) {
            case REGISTER -> "LAB_VERIFY · ĐĂNG KÝ";
            case RESET_PASSWORD -> "LAB_VERIFY · ĐẶT LẠI MK";
            case CHANGE_PASSWORD -> "LAB_VERIFY · ĐỔI MK";
        };
    }

    private String purposeCta(OtpPurpose purpose) {
        return switch (purpose) {
            case REGISTER -> "Mở trang xác thực OTP";
            case RESET_PASSWORD -> "Mở trang đặt lại mật khẩu";
            case CHANGE_PASSWORD -> "Quay lại BioVerse";
        };
    }

    private static String trimTrailingSlash(String url) {
        if (url == null || url.isBlank()) {
            return "https://bioverse.eraidev.id.vn";
        }
        String trimmed = url.trim();
        while (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }
}
