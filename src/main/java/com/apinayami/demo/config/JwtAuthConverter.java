package com.apinayami.demo.config;

import com.apinayami.demo.exception.CustomException;
import lombok.extern.java.Log;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Lớp này dùng để convert role trong token sang GrantedAuthoritiesConverter
 * */
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String CLAIM_ROLES = "roles"; // Đây là claim chứa quyền trong token

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        if (jwt == null) {
            System.out.println("From JwtAuthConverter: Jwt is null");
        }else{
            try {
                Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
                return new JwtAuthenticationToken(jwt, authorities);
            } catch (Exception e) {
                System.out.println("From JwtAuthConverter: Jwt is not valid");
            }
        }
        return null;
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList(CLAIM_ROLES);
        if (roles == null) {
            return List.of();
        }
        return roles.stream()
                .map(role -> role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}

