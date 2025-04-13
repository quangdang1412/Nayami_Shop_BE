package com.apinayami.demo.service.Impl;

import com.apinayami.demo.config.JwtConfig;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.model.CustomUserDetail;
import com.apinayami.demo.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final JwtConfig jwtConfig;
    private final SecurityUtil securityUtil;
    private final UserDetailCustomService userDetailCustomService;

    public String createNewAccessToken(String refreshToken) {
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            throw new CustomException("Unauthorized token");
        }
        String rawRefreshToken = refreshToken.substring(7);

        Jwt decodedToken = jwtConfig.decodeToken(rawRefreshToken);
        String email = decodedToken.getSubject();
        List<String> roles = decodedToken.getClaim("role_of_user");
        Collection<GrantedAuthority> authorities = roles.stream()
                .map(role -> role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());


        CustomUserDetail userDetail = new CustomUserDetail(email,"",authorities,"");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetail, null, authorities);

        return securityUtil.createToken(authentication);
    }
}
