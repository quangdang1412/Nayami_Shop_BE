package com.apinayami.demo.service.Impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public boolean sendEmail(String toEmail, String subject, String content) throws MessagingException {
        boolean result = false;
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            result = true;
        } catch (MessagingException e) {
            throw new MessagingException(e.getMessage());
        }
        return result;
    }

    public boolean sendResetPassEmail(String toEmail, String subject, String linkToResetPassword, String userName) throws MessagingException {
        boolean result = false;
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            Context context = new Context();
            Map<String, Object> properties = new HashMap<>();
            properties.put("linkToResetPassword", linkToResetPassword);
            properties.put("userName", userName);
            context.setVariables(properties);
            String html = templateEngine.process("ResetPasswordMail", context);
            helper.setText(html, true);
            mailSender.send(message);
            result = true;
        } catch (MessagingException e) {
            throw new MessagingException(e.getMessage());
        }
        return result;
    }

    public boolean checkEmail(String email) {
        String key = "6WKz43peqGegMRVw0TVvx";
        String url = "https://apps.emaillistverify.com/api/verifyEmail?secret=" + key + "&email=" + email;
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getBody().equalsIgnoreCase("ok")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true; // Nếu dịch vụ kiểm tra mail bị lỗi bỏ qua việc kiểm tra
        }
    }
}

