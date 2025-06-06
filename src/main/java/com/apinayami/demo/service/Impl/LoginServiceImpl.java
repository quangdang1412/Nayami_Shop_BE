package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.LoginDTO;
import com.apinayami.demo.dto.response.ResLoginDTO;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;

    public ResLoginDTO login(LoginDTO loginDTO) {
        try{
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            String accessToken = securityUtil.createToken(authentication);
            String refreshToken = securityUtil.createRefreshToken(authentication);
            ResLoginDTO resLoginDTO = new ResLoginDTO(accessToken, refreshToken);
            return resLoginDTO;
        }catch (Exception e){
            if (e instanceof CustomException)
                throw  new CustomException(e.getMessage());
            throw  new CustomException("Đăng nhập không thành công");
        }

    }
}
