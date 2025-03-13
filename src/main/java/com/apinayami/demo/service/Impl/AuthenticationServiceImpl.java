package com.apinayami.demo.service.Impl;


import com.apinayami.demo.dto.request.AuthenticationRequest;
import com.apinayami.demo.dto.request.RegisterRequest;
import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.dto.response.AuthenticationResponse;
import com.apinayami.demo.exception.ResourceNotFoundException;
import com.apinayami.demo.mapper.UserMapper;
import com.apinayami.demo.model.TokenModel;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.IUserRepository;
import com.apinayami.demo.service.IAuthenticationService;
import com.apinayami.demo.service.IUserService;
import com.apinayami.demo.util.Enum.Role;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final IUserRepository userRepository;
    private final TokenServiceImpl tokenService;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final IUserService userService;
    private final UserMapper userMapper;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new ResourceNotFoundException("Email đã đăng kí tài khoản: " + request.getEmail());
        }
        if (request.getPassword().length() < 6) {
            throw new ResourceNotFoundException("Mật khẩu phải dài hơn hoặc bằng 6 kí tự");
        }
        if (userService.existsByPhoneNumber(request.getPhone())) {
            throw new ResourceNotFoundException("SĐT đã đăng kí tài khoản: " + request.getPhone());
        }

        UserModel newUser = new UserModel();
        // String userId = "U" + UUID.randomUUID().toString().substring(0, 8);
        // newUser.setUserID(userId);

        newUser.setUserName(request.getName());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setPhoneNumber(request.getPhone());
        newUser.setType(Role.CUSTOMER);

        UserModel createdUser = userRepository.save(newUser);
        String jwtToken = jwtService.generateToken(createdUser);

        TokenModel token = TokenModel.builder()
                .email(createdUser.getEmail())
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.save(token);

        return AuthenticationResponse.builder()
                .userDto(userMapper.toDetailDto(createdUser))
                .token(jwtToken)
                .build();
    }

    // @Override
    // public AuthenticationResponse refreshToken(RefreshRequest refreshTokenRequest) {
    //     String refreshToken = refreshTokenRequest.getRefreshToken();
    //     final String email = jwtService.extractUsername(refreshToken);
    //     Token token = tokenService.tokenRepository().findByEmail(email).get();

    //     if (token == null || token.isRevoked() || token.isExpired()) {
    //         throw new TokenException("Invalid or expired refresh token");
    //     }
    //     UserModel user = userService.findByEmail(token.getEmail());
    //     String newAccessToken = jwtService.generateToken(user);
    //     token.setToken(newAccessToken);
    //     token.setExpired(false);
    //     token.setRevoked(false);
    //     tokenService.save(token);
    //     return AuthenticationResponse.builder()
    //             .userDto(UserMapper.mapToUserDto(user))
    //             .token(newAccessToken)
    //             .build();
    // }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request, String typeLogin) {
        if (typeLogin.equals("normal")) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
        }
        UserModel user = userService.getUserByEmail(request.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException("Tài khoản hoặc mật khẩu không chính xác");
        }
        // if (request.getPassword().user.getPassword())) {
        // throw new ResourceNotFoundException("Tài khoản hoặc mật khẩu không chính
        // xác");
        // }
        String jwtToken = jwtService.generateToken(user);
        TokenModel token = TokenModel.builder()
                .email(user.getEmail())
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.save(token);
        UserDTO userDto = userMapper.toDetailDto(user);
        return AuthenticationResponse.builder()
                .userDto(userDto)
                .token(jwtToken)
                .build();
    }

    // @Override
    // public boolean logout(String token) {
    //     final String email = jwtService.extractUsername(token);
    //     TokenModel tokenEntity = tokenService.tokenRepository().findByEmail(email).get();

    //     if (tokenEntity == null) {
    //         return false;
    //     }
    //     tokenEntity.setRevoked(true);
    //     tokenService.removeToken(tokenEntity);
    //     return tokenService.removeToken(tokenEntity);
    // }

    // @Override
    // public String forgotPassword(String email, String randomString) throws
    // MessagingException, UnsupportedEncodingException {
    // try {
    // if (userService.existsByEmail(email)) {
    // String confirmLink = "http://localhost:8080/reset-password?email=" + email +
    // "&resetPasswordKey=" + randomString;
    // mailService.sendResetPasswordMail(confirmLink, email);
    // return "Success";
    // } else
    // return "Failed";
    // } catch (Exception e) {
    // return "Failed";
    // }
    // }
}