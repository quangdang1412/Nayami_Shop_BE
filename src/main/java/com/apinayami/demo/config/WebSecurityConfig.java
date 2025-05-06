package com.apinayami.demo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {

    /*
     * Những endpoint mà ai cũng có thể truy cập, trong trường hợp là là những unregistered customer, lúc gọi đến này không cần token
     * */
    private final String[] PUBLIC_API = {
            "/api/login",
            "/api/signup",
            "/api/users/**",
            "/api/users/check",
            "/api/users/create",
            "/api/reset-password",
            "/api/check-email-exist",
            "/api/reset-password",
            "/api/categories/**",
            "/api/products/**",
            "/api/comments/**",
            "/api/responses/**",
            "/api/promotions/**",
            "/api/auth/social-login/google",
            "/api/login/oauth2/code/google",
//            Test purposes

            //Swagger
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/webjars/**",
    };

    private final String[] ADMIN_API = {
            "/api/brands",
    };
    private final String[] CUSTOMER_API = {
    };
    private final String[] STAFF_API = {

    };
    private final String[] REFRESH_TOKEN = {
            "/api/refresh"
    };
    private final String[] REFRESH_PASSWORD_TOKEN = {
            "/api/reset-password/check-token",
            "/api/reset-password/entered",
    };


    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                        .requestMatchers(PUBLIC_API).permitAll()
                        .requestMatchers(ADMIN_API).hasAuthority("ADMIN")
                        .requestMatchers(CUSTOMER_API).hasAuthority("CUSTOMER")
                        .requestMatchers(STAFF_API).hasAuthority("STAFF")
                        .requestMatchers(REFRESH_TOKEN).hasAuthority("REFRESH_TOKEN")
                        .requestMatchers(REFRESH_PASSWORD_TOKEN).hasAuthority("REFRESH_PASSWORD_TOKEN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(
                        exception -> exception.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint()) // 401
                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()) // 403
                                .authenticationEntryPoint(customAuthenticationEntryPoint)

                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthConverter())))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }


    //
//
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "https://nayami-shop-fe.vercel.app"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}