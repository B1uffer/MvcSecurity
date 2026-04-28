package com.b1uffer.securitymvc.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

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

    /**
     * 경로 매칭 전략, MVC 패턴
     * MvcRequestMatcher는 3.5.x 기준으로 더이상 사용되지 않는다
     */
//    @Bean
//    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
//        return new MvcRequestMatcher.Builder(introspector);
//    }
//
//    @Bean
//    SecurityFilterChain springSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers(mvc().pattern("/articles/{id}")).hasRole("USER") // 경로 변수 인식
//                .requestMatchers(mvc().pattern(HttpMethod.POST, "/articles")).hasRole("WRITER")
//                .anyRequest().authenticated()
//        );
//        return http.build();
//    }
}
