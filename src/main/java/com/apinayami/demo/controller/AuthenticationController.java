package com.apinayami.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.apinayami.demo.dto.request.AuthenticationRequest;
import com.apinayami.demo.dto.request.RegisterRequest;
import com.apinayami.demo.dto.response.AuthenticationResponse;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.service.IUserService;
import com.apinayami.demo.service.Impl.AuthenticationServiceImpl;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;
    // private final CheckLogin checkLogin;
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            AuthenticationResponse response = authenticationService.register(registerRequest);
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + response.getToken()) // Thêm token vào header
                    .body(response);
        } catch (Exception e) {
            if (e instanceof ResourceNotFoundException)
                return ResponseEntity.status(409).body(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // @PostMapping("/refresh")
    // public ResponseData<?> refresh(@RequestHeader("Authorization") String bearerToken) {
    //     try {
    //         String token = bearerToken.replace("Bearer ", "");
    //         RefreshRequest refreshTokenRequest = new RefreshRequest(token);
    //         AuthenticationResponse response = authenticationService.refreshToken(refreshTokenRequest);
    //         return new ResponseData<>(HttpStatus.CREATED.value(), "Success", response);
    //     } catch (Exception e) {
    //         return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    //     }
    // }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = authenticationService.login(request, "normal");
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + response.getToken()) // Thêm token vào header
                    .body(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // @PostMapping("/logout")
    // public ResponseEntity<String> logout(@RequestHeader("Authorization") String token, HttpServletRequest httpServletRequest) {
    //     // Xóa token trong cơ sở dữ liệu
    //     boolean success = authenticationService.logout(token.substring(7)); // Loại bỏ "Bearer " trước token
    //     if (success) {
    //         HttpSession session = httpServletRequest.getSession(false); // Get the existing session, if any
    //         if (session != null) {
    //             session.invalidate(); // Invalidate the session
    //         }
    //         return ResponseEntity.ok("Đăng xuất thành công!");
    //     } else {
    //         return ResponseEntity.status(400).body("Không tìm thấy token!");
    //     }
    // }

  
    // @PostMapping("/forgot_password")
    // public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request, HttpSession session) throws MessagingException, UnsupportedEncodingException {
    //     String email = request.get("email");
    //     String uuid = UUID.randomUUID().toString();  // Tạo UUID ngẫu nhiên
    //     String randomString = uuid.substring(0, 6);
    //     String response = authenticationService.forgotPassword(email, randomString);
    //     if ("Success".equals(response)) {
    //         session.setAttribute("resetPasswordToken", randomString);
    //         return ResponseEntity.ok("Password reset link sent successfully.");
    //     } else {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found.");
    //     }
    // }

    // @PutMapping("/resetPassword")
    // public ResponseData<String> resetPassword(@RequestBody Map<String, String> allParams, HttpSession session) {
    //     try {
    //         if (!session.getAttribute("resetPasswordToken").equals(allParams.get("resetPasswordKey")))
    //             return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Thất bại");
    //         String email = allParams.get("email");
    //         String newPassword = allParams.get("newPassword");
    //         String confirmPassword = allParams.get("confirmPassword");
    //         UserModel user = userService.findByEmail(email);
    //         if (user == null) {
    //             return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Người dùng không tồn tại.");
    //         }
    //         if (!newPassword.equals(confirmPassword)) {
    //             return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Mật khẩu mới và mật khẩu xác nhận không khớp.");
    //         }
    //         user.setPassword(passwordEncoder.encode(newPassword));
    //         String userId = userService.changePassword(user.getUserID(), user.getPassword());
    //         session.setAttribute("resetPasswordToken", null);
    //         return new ResponseData<>(HttpStatus.CREATED.value(), "Success", userId);
    //     } catch (Exception e) {
    //         return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Fail");
    //     }
    // }


} 