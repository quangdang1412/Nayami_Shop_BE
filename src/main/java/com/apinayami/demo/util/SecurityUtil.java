package com.apinayami.demo.util;

import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecurityUtil {
    private final JwtEncoder jwtEncoder;
    public static MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    private final long JWR_EXPIRATION_ACCESSTOKEN = 30;
    private final long JWR_EXPIRATION_REFRESHTOKEN = 7*24*60*60;


    public String createToken(Authentication authentication){
        //Thoi gian het han
        Instant now = Instant.now();
        Instant validity = Instant.now().plus(JWR_EXPIRATION_ACCESSTOKEN, ChronoUnit.SECONDS);


        var role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        JwtClaimsSet claims = JwtClaimsSet.builder().
                issuedAt(now).
                expiresAt(validity).
                claim("roles",role).
                subject(authentication.getName())
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,claims)).getTokenValue();
    }
    public String createRefreshToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant validity = now.plus(JWR_EXPIRATION_REFRESHTOKEN, ChronoUnit.SECONDS);
        var role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .claim("role_of_user",role)
                .claim("roles","REFRESH_TOKEN")
                .subject(authentication.getName())
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

}
