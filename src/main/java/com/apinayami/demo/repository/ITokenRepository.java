package com.apinayami.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apinayami.demo.model.TokenModel;

@Repository
public interface ITokenRepository extends JpaRepository<TokenModel, Integer> {

    TokenModel findByToken(String refreshToken);

    Optional<TokenModel> findByEmail(String email);

    TokenModel removeTokenByToken(String token);

}