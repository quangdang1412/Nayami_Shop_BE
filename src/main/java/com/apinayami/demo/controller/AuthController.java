package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.LoginDTO;
import com.apinayami.demo.dto.request.SignupDTO;
import com.apinayami.demo.dto.response.ResLoginDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.service.Impl.LoginServiceImpl;
import com.apinayami.demo.service.Impl.RefreshTokenService;
import com.apinayami.demo.service.Impl.SignupServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final LoginServiceImpl loginService;

    @PostMapping("/api/login")
    public ResponseData<ResLoginDTO> login(@RequestBody LoginDTO loginDTO) {
        ResLoginDTO resLoginDTO = loginService.login(loginDTO);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Login Successfully", resLoginDTO);
    }

    private final SignupServiceImpl signupService;

    @PostMapping("/api/signup")
    public ResponseData<Void> signup(@RequestBody SignupDTO signupDTO) {
        try {
            boolean isRegistered = signupService.register(signupDTO);
            if (isRegistered) {
                return new ResponseData<>(HttpStatus.CREATED.value(), "Đăng ký thành công");
            } else {
                return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Đăng ký không thành công");
            }
        } catch (Exception e) {
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Đăng ký không thành công");
        }
    }

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/api/refresh")
    public ResponseData<String> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String newAccessToken = refreshTokenService.createNewAccessToken(authHeader);
            return new ResponseData<>(HttpStatus.CREATED.value(), "New access token", newAccessToken);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Invalid refresh token");
        }
    }


}
