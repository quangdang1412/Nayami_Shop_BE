package com.apinayami.demo.service.Impl;



import java.security.Key;
import java.util.Date;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.apinayami.demo.model.TokenModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.ITokenRepository;
import com.apinayami.demo.service.IJwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Function;
import io.jsonwebtoken.SignatureAlgorithm;


import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements IJwtService {
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private final ITokenRepository tokenRepository;

    @Override
    public String extractUsername(String token) {
        Claims claims = extractClaims(token);
        if (claims != null) {
            Date expirationTime = claims.getExpiration();
            boolean isExpired = expirationTime.before(Date.from(Instant.now()));
            if (!isExpired) {
                return claims.getSubject();
            } else return null;
        }
        return null;
    }

    @Override
    public String generateToken(UserModel user) {
        return Jwts
                .builder()
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isValid(String token, UserModel user) {
        try {
            final String email = extractUsername(token);
            Optional<TokenModel> tokenOptional = tokenRepository.findByEmail(email);
            boolean checkToken = tokenOptional.get().getToken().equals(token);
            return (email.equals(user.getEmail()) && !isTokenExpired(token) && checkToken);
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                log.error("Token Expired: " + e.getMessage());
            }
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}