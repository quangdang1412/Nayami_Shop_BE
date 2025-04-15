package com.apinayami.demo.service.Impl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    public boolean sendEmail(String toEmail, String subject,String content) throws MessagingException
    {
        boolean result = false;
        try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(toEmail);
                helper.setSubject(subject);
                helper.setText(content,true);
                mailSender.send(message);
                result = true;
            }catch (MessagingException e) {
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
            }else{
                return false;
            }
        }catch (Exception e) {
            return true; // Nếu dịch vụ kiểm tra mail bị lỗi bỏ qua việc kiểm tra
        }
    }
}

