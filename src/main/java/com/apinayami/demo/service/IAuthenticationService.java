package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.AuthenticationRequest;
import com.apinayami.demo.dto.request.RegisterRequest;
import com.apinayami.demo.dto.response.AuthenticationResponse;

public interface IAuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse login(AuthenticationRequest request, String type);
    // AuthenticationResponse refreshToken(RefreshRequest request);
}