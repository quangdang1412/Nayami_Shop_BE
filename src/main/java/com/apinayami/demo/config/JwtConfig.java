package com.apinayami.demo.config;

import com.apinayami.demo.util.SecurityUtil;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.FilterChainProxy;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*
* Lớp này dùng để cấu hình jwt encoder và decoder
* */
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class JwtConfig {
    @Value("${JWT_SECRET_KEY}")
    private String jwtSecretKey;
    private SecretKey getSecretKey(){
        byte[] keyBytes = Base64.from(jwtSecretKey).decode(); //giai ma
        return new SecretKeySpec(keyBytes, 0,keyBytes.length,SecurityUtil.JWT_ALGORITHM.getName());
    }
    @Bean
    public JwtEncoder jwtEncoder(){
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }
    @Bean
    public JwtDecoder jwtDecoder(){
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.
                withSecretKey(getSecretKey()).
                macAlgorithm(SecurityUtil.JWT_ALGORITHM).
                build();
        return token ->{
           try{
               Jwt jwt = jwtDecoder.decode(token);
               return jwt;
           }catch(Exception e){
               throw new RuntimeException(e);
           }
        };
    }
    public Jwt decodeToken(String token){
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.
                withSecretKey(getSecretKey()).
                macAlgorithm(SecurityUtil.JWT_ALGORITHM).
                build();
        return jwtDecoder.decode(token);
    }


}
