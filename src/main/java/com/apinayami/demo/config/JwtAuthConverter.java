package com.apinayami.demo.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
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
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList(CLAIM_ROLES); // Lấy danh sách roles từ claim
        if (roles == null) {
            return List.of();
        }
        System.out.println(jwt.getClaims());
        return roles.stream()
                .map(role -> role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}

