package com.apinayami.demo.controller;

import com.apinayami.demo.dto.response.ResLoginDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.IUserRepository;
import com.apinayami.demo.service.Impl.OAuthService;
import com.apinayami.demo.service.Impl.UserServiceImpl;
import com.apinayami.demo.util.Enum.Role;
import com.apinayami.demo.util.SecurityUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class OAuth2Controller {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;



    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    private final OAuthService authService;
    private final UserServiceImpl userService;
    private final SecurityUtil securityUtil;
    private final IUserRepository userRepository;

    @GetMapping("/api/auth/social-login/google")
    public ResponseData<String> loginWithGoogle() {
        try{
            String url = authService.generateAuthUrl();
            return new ResponseData<>(HttpStatus.OK.value(), "Url",url);
        }
        catch(Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @GetMapping("/api/login/oauth2/code/google")
    public ResponseData<ResLoginDTO>  googleCallback(@RequestParam Map<String, String> map, HttpServletRequest httpServletRequest)
    {
        try {
            ResLoginDTO resLoginDTO = authService.getCredentialOfUser(map);
            return new ResponseData<>(HttpStatus.OK.value(),"Success",resLoginDTO);
        }catch (Exception e){
            if (e instanceof CustomException)
                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Đăng ký không thành công");
        }

    }

}
