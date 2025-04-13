package com.apinayami.demo.service.Impl;


import org.springframework.stereotype.Service;
import com.apinayami.demo.model.TokenModel;
import com.apinayami.demo.repository.ITokenRepository;
import java.util.Optional;

@Service
public record TokenServiceImpl(ITokenRepository tokenRepository) {
    public void save(TokenModel token) {
        Optional<TokenModel> tokenOptional = tokenRepository.findByEmail(token.getEmail());
        if (tokenOptional.isEmpty()) {
            tokenRepository.save(token);
        } else {
            TokenModel currentToken = tokenOptional.get();
            currentToken.setToken(token.getToken());
            tokenRepository.save(currentToken);
        }
    }

    public boolean removeToken(TokenModel token) {
        tokenRepository.delete(token);
        return true;
    }

}