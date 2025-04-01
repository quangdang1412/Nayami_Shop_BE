package com.apinayami.demo.service.Impl;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
@Slf4j
public class EmailServiceImpl {
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public String sendEmail(String toEmail, String subject, String content) {
        Email from = new Email("tanvinh58@gmail.com");
        Email to = new Email(toEmail);
        Content emailContent = new Content("text/plain", content);
        Mail mail = new Mail(from, subject, to, emailContent);
        log.error("errorMessage={}",sendGridApiKey);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            log.info("Response status code: {}", response.getStatusCode());
            log.info("Response body: {}", response.getBody());
            log.info("Response headers: {}", response.getHeaders());
            return "Email sent with status code: " + response.getStatusCode();
        } catch (IOException ex) {
            return "Error sending email: " + ex.getMessage();
        }
    }
}
