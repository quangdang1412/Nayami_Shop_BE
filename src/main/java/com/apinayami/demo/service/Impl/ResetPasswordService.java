package com.apinayami.demo.service.Impl;

import com.apinayami.demo.util.SecurityUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {
    private final String domainFrontEnd = "http://localhost:5173/reset-password/";
    private final SecurityUtil securityUtil;
    private final EmailService emailService;
    private final UserServiceImpl userService;

    private String generateLinkResetPassword(String email) {
        String resetPassWordToken = securityUtil.createResetPasswordToken(email);
        String linkToResetPassword = domainFrontEnd + "?token=" + resetPassWordToken;
        return linkToResetPassword;
    }

    public boolean sendLinkResetPassword(String email) {
        String linkToResetPassword = generateLinkResetPassword(email);
        String emailContent = String.format("""
                <html>
                <head><title>Reset Password</title></head>
                <body>
                    <div style="text-align: center; font-family: Arial, sans-serif;">
                        <h2>Reset Your Password</h2>
                        <p>Click the button below to reset your password:</p>
                        <a href="%s" style="padding: 10px 20px; background: #007bff; color: #fff; text-decoration: none; border-radius: 5px;">Reset Password</a>
                        <p>If you didn't request a password reset, please ignore this email.</p>
                    </div>
                </body>
                </html>
                """, linkToResetPassword);
        try {
            if (emailService.checkEmail(email)) {
                if (userService.checkUserExistByEmail(email)) {
                    boolean isSendLinkToEmailSuccessfully = emailService.sendResetPassEmail(email, "Your link to reset password: ", linkToResetPassword, userService.getUserByEmail(email).getUserName());
                    return isSendLinkToEmailSuccessfully;
                }
            }
            return false;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
