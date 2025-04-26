package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.UserDTO;
import com.apinayami.demo.dto.response.ResLoginDTO;
import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.model.UserModel;
import com.apinayami.demo.repository.IUserRepository;
import com.apinayami.demo.util.Enum.Role;
import com.apinayami.demo.util.SecurityUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthServiceImpl {
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final UserServiceImpl userService;
    private final SecurityUtil securityUtil;
    private final IUserRepository userRepository;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;


    public String generateAuthUrl() {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
        OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
                .clientId(clientRegistration.getClientId())
                .redirectUri(redirectUri)
                .scope("openid", "profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .state("random-state-value") // Thêm trạng thái bảo mật (tùy chọn)
                .build();
        return authorizationRequest.getAuthorizationRequestUri();
    }

    public ResLoginDTO getCredentialOfUser(Map<String, String> map) {
        try {
            String code = map.get("code");
            String accessTokenWithGoogle = getAccessToken(code);
            UserModel userInfo = getUserInfo(accessTokenWithGoogle);

            UserDTO currentUser = userService.getUserByEmail(userInfo.getEmail());
            if (currentUser == null) { // Neu user khong ton tai
                userRepository.save(userInfo);
            }else{ // Neu user ton tai
                if(currentUser.isActive() == false) //user da bi vo hieu hoa
                {
                    throw new CustomException("Tài khoản đã bị vô hiệu hóa");
                }
            }
            String accessTokenForUser = securityUtil.createAcessTokenForOauth(userInfo.getType(), userInfo.getUsername(), userInfo.getEmail());
            String refreshTokenForUser = securityUtil.createRefreshTokenOauth(userInfo.getType(), userInfo.getEmail());
            return new ResLoginDTO(accessTokenForUser, refreshTokenForUser);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    private String getAccessToken(String authorizationCode) {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);

        // Use ObjectMapper to parse the JSON response and extract the access token
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText(); // Extract the access token
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public UserModel getUserInfo(String accessToken) {
        final String defaultPassword = "123456";


        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return UserModel.builder()
                    .userName(jsonNode.get("name").asText())
                    .email(jsonNode.get("email").asText())
                    .type(Role.CUSTOMER)
                    .active(true)
                    .password(defaultPassword)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
}
