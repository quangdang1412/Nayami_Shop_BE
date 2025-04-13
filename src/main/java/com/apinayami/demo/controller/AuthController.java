package com.apinayami.demo.controller;

import com.apinayami.demo.config.JwtConfig;
import com.apinayami.demo.dto.request.LoginDTO;
import com.apinayami.demo.dto.request.SignupDTO;
import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.dto.response.ResLoginDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.mapper.UserMapper;
import com.apinayami.demo.service.IUserService;
import com.apinayami.demo.service.Impl.LoginService;
import com.apinayami.demo.service.Impl.RefreshTokenService;
import com.apinayami.demo.service.Impl.SignupService;
import com.apinayami.demo.util.SecurityUtil;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final LoginService loginService;
    @PostMapping("/api/login")
    public ResponseData<ResLoginDTO> login(@RequestBody LoginDTO loginDTO) {
        ResLoginDTO resLoginDTO = loginService.login(loginDTO);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Login Successfully",resLoginDTO);
    }

    private final SignupService signupService;
    @PostMapping("/api/signup")
    public ResponseData<Void> signup(@RequestBody SignupDTO signupDTO) {
        try{
            boolean isRegistered = signupService.register(signupDTO);
            if(isRegistered) {
                return new ResponseData<>(HttpStatus.CREATED.value(), "Đăng ký thành công");
            }else{
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
            return new ResponseData<>(HttpStatus.CREATED.value(), "New access token",newAccessToken);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Invalid refresh token");
        }
    }


}
