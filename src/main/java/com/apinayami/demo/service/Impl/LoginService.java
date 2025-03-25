package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.LoginDTO;
import com.apinayami.demo.dto.response.ResLoginDTO;
import com.apinayami.demo.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    public ResLoginDTO login(LoginDTO loginDTO) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String accessToken = securityUtil.createToken(authentication);
        String refreshToken = securityUtil.createRefreshToken(authentication);
        ResLoginDTO resLoginDTO = new ResLoginDTO(accessToken,refreshToken);
        return resLoginDTO;
    }
}
