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

    public MailService(
            JavaMailSender mailSender,
            @Qualifier("emailTemplateEngine") SpringTemplateEngine emailTemplateEngine,
            @Value("${bioverse.mail.from:}") String from,
            @Value("${bioverse.mail.from-name:Bioverse}") String fromName
    ) {
        this.mailSender = mailSender;
        this.emailTemplateEngine = emailTemplateEngine;
        this.from = from;
        this.fromName = fromName;
    }

    public void sendOtp(String to, String fullName, String otp, OtpPurpose purpose, long expireMinutes) {
        if (from == null || from.isBlank()) {
            throw new AppException(ErrorCode.EMAIL_SEND_FAILED, "Chưa cấu hình MAIL_FROM / MAIL_USERNAME");
        }

        Context context = new Context(Locale.forLanguageTag("vi"));
        context.setVariable("otp", otp);
        context.setVariable("fullName", displayName(fullName));
        context.setVariable("expireMinutes", expireMinutes);
        context.setVariable("year", Year.now().getValue());
        context.setVariable("purpose", purpose.name());
        context.setVariable("purposeTitle", purposeTitle(purpose));
        context.setVariable("purposeHint", purposeHint(purpose));

        String html = emailTemplateEngine.process("mail/otp", context);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setSubject(purposeSubject(purpose));
            helper.setFrom(new InternetAddress(from, fromName, StandardCharsets.UTF_8.name()));
            helper.setText(html, true);
            mailSender.send(message);
            log.info("OTP email sent to {} for {}", to, purpose);
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
        return purpose == OtpPurpose.REGISTER
                ? "Mã xác thực đăng ký Bioverse"
                : "Mã đặt lại mật khẩu Bioverse";
    }

    private String purposeTitle(OtpPurpose purpose) {
        return purpose == OtpPurpose.REGISTER
                ? "Xác thực đăng ký tài khoản"
                : "Đặt lại mật khẩu";
    }

    private String purposeHint(OtpPurpose purpose) {
        return purpose == OtpPurpose.REGISTER
                ? "Nhập mã này để hoàn tất đăng ký tài khoản Bioverse."
                : "Nhập mã này để xác thực và đặt lại mật khẩu Bioverse.";
    }
}
