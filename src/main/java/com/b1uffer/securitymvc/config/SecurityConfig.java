package com.b1uffer.securitymvc.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers("/admin/**").access(AuthorityAuthorizationManager.hasRole("ADMIN"))
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").access(AuthorityAuthorizationManager.hasRole("USER"))
//                        .requestMatchers("/user/**").hasRole("USER")
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()))
                .formLogin(login -> login.permitAll());

        return http.build();
    }
}
