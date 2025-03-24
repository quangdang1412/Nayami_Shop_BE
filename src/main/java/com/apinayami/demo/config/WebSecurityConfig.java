package com.apinayami.demo.config;

import com.apinayami.demo.util.Enum.Role;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {

//     private final AuthenticationFilter authenticationFilter;
//     private final AuthenticationProvider authenticationProvider;
//     private final String[] WHITE_LITS = {"/",
//     "/static/**",
//     "/template/**",
//     "/signinoauth2",
//     "/loginSuccess",
//     "/error",
//     "/api/auth/**",
//     "/assets/**",
//     "/asset-admin/**",
//     "/signup",
//     "/register",
//     "/shop",
//     "/ImageProduct/**",
//     "/api/addtocart/**",
//     "/detail/**",
//     "/locations",
//     "/login/**",
//     "/inforuser",
//     "/changePassword",
//     "/reset-password",
//     "/yourOrder/**",
//     "/cart",
//     "/checkout",
//     "/admin/**",
//     "/test",
//     "/api/payments/**",
//     "/search",
//     "api/user/checkCoupon/**"
//     };
//     private final String[] EMPLOYEE_LIST = {
//     "/api/order/update",
//     "/api/product/**",
//     "/api/dashboard/**",
//     };
//     private final String[] ADMIN_LIST = {
//     "/api/order/update",
//     "/api/order/delete/**",
//     "/api/other/**",
//     "/api/product/add",
//     "/api/product/update",
//     "/api/dashboard/**",
//     "/api/product/delete/**",
//     "/api/supplier/**",
//     "/api/user/add",
//     "/api/user/delete/**",
//     "/api/user/update"
//     };
//
//         @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
//     Exception {
//         http
//         .authorizeHttpRequests(requests -> requests
//         .requestMatchers(WHITE_LITS).permitAll()
//         .requestMatchers(EMPLOYEE_LIST).hasAnyAuthority(role_more)
//         .requestMatchers(ADMIN_LIST).hasAuthority("ADMIN")
//         .anyRequest().authenticated()
//         )
//         .oauth2Login(Customizer.withDefaults())
//         .sessionManagement(session -> session
//         .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//         .formLogin(Customizer.withDefaults())// Sử dụng trang login mặc định của
//         Spring Security
//         // .addFilterBefore(authenticationFilter,
//         UsernamePasswordAuthenticationFilter.class) // Thêm JWT filter
//        // .authenticationProvider(authenticationProvider) // Cấu hình provider
//         .exceptionHandling(exception -> exception
//         .authenticationEntryPoint((request, response, authException) ->
//         response.sendError(403, "Access Denied")
//         )
//         );
//         http.exceptionHandling(exceptionHandling -> exceptionHandling
//         .accessDeniedPage("/404")
//         );
//         http.csrf(AbstractHttpConfigurer::disable);
//         return http.build();
//     }

    /*
    * Những endpoint mà ai cũng có thể truy cập, trong trường hợp là là những unregistered customer, lúc gọi đến này không cần token
    * */
    private final String[] PUBLIC_API = {
            "/api/users", // dùng để thêm user tại chưa có tính năng đăng ký
            "/api/login",
            "/api/signup",
            "/api/products/get-all-product",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/webjars/**"
    };

    private final String[] ADMIN_API = {
            "/api/brands/**",
            "/api/products/**",
            "/api/categories/**"
    };

    private final String[] CUSTOMER_API = {

    };
    private final String[] STAFF_API = {
    };
    private final String[] REFRESH_TOKEN = {
            "/api/refresh"
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(PUBLIC_API).permitAll()
                        .requestMatchers(ADMIN_API).hasAuthority("ADMIN")
                        .requestMatchers(CUSTOMER_API).hasAuthority("CUSTOMER")
                        .requestMatchers(STAFF_API).hasAuthority("STAFF")
                        .requestMatchers(REFRESH_TOKEN).hasAuthority("REFRESH_TOKEN")
                        .anyRequest().authenticated()
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

    //Ai cung co the truy cap

}
