package com.apinayami.demo.service;

import com.apinayami.demo.model.UserModel;

public interface IJwtService {
    String generateToken(UserModel user);

    String extractUsername(String token);

    boolean isValid(String token, UserModel user);

}