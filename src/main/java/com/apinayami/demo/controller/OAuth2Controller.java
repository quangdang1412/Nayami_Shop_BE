package com.apinayami.demo.controller;

import com.apinayami.demo.dto.response.ResLoginDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.service.Impl.OAuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuthServiceImpl authService;

    @GetMapping("/api/auth/social-login/google")
    public ResponseData<String> loginWithGoogle() {
        try {
            String url = authService.generateAuthUrl();
            return new ResponseData<>(HttpStatus.OK.value(), "Url", url);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @GetMapping("/api/login/oauth2/code/google")
    public ResponseData<ResLoginDTO> googleCallback(@RequestParam Map<String, String> map, HttpServletRequest httpServletRequest) {
        try {
            ResLoginDTO resLoginDTO = authService.getCredentialOfUser(map);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", resLoginDTO);
        } catch (Exception e) {
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Đăng ký không thành công");
        }

    }

}
