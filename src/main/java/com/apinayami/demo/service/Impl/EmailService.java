package com.apinayami.demo.service.Impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.apinayami.demo.dto.response.BillDetailDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public boolean sendEmail(String toEmail, String subject, String content) throws MessagingException {
        boolean result = false;
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
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

    public boolean sendResetPassEmail(String toEmail, String subject, String linkToResetPassword, String userName)
            throws MessagingException {
        boolean result = false;
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
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

    @Async
    public CompletableFuture<Boolean> sendEmailOrder(String toEmail, String subject, BillDetailDTO billDetailDTO)
            throws IOException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);

            Context context = new Context();
            Map<String, Object> properties = new HashMap<>();
            properties.put("phone", billDetailDTO.getShipping().getAddress().getPhone());
            properties.put("status", subject);
            properties.put("name", billDetailDTO.getShipping().getAddress().getRecipientName());
            properties.put("address", billDetailDTO.getShipping().getAddress().getAddressName() + ", "
                    + billDetailDTO.getShipping().getAddress().getWard() + ", "
                    + billDetailDTO.getShipping().getAddress().getDistrict() + ", "
                    + billDetailDTO.getShipping().getAddress().getProvince());

            properties.put("orderItems", billDetailDTO.getItems());
            double subTotal = 0.0;
            for (int i = 0; i < billDetailDTO.getItems().size(); i++) {
                subTotal += billDetailDTO.getItems().get(i).getUnitPrice()
                        * billDetailDTO.getItems().get(i).getQuantity();
            }
            properties.put("subTotal", subTotal);
            properties.put("shippingFee", billDetailDTO.getShipping().getShippingFee());
            double discount = billDetailDTO.getDiscount() == null ? 0.0 : billDetailDTO.getDiscount();
            properties.put("coupon", discount);
            Double total = billDetailDTO.getTotalPrice() - discount + billDetailDTO.getShipping().getShippingFee();
            properties.put("total", total);

            context.setVariables(properties);
            String content = templateEngine.process("OrderMail", context);

            helper.setText(content, true);
            mailSender.send(message);

            return CompletableFuture.completedFuture(true);
        } catch (MessagingException e) {
            return CompletableFuture.completedFuture(false);
        }
    }

}
