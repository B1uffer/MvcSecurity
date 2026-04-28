package com.b1uffer.securitymvc.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/admin/**").access(AuthorityAuthorizationManager.hasRole("ADMIN"))
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").access(AuthorityAuthorizationManager.hasRole("USER"))
//                        .requestMatchers("/user/**").hasRole("USER")
//                        .requestMatchers(new RegexRequestMatcher("^/file/[a-f0-9\\\\-]{36}$", null)).hasAuthority("FILE_READ")
                        /**
                         * implementation 'org.springframework.boot:spring-boot-starter-actuator'
                         */
//                        .requestMatchers(EndpointRequest.to("health", "info")).permitAll()
//                        .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
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

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long id) {
        // ADMIN 권한을 가진 사용자만 실행 가능한 메서드
    }

    @PostAuthorize("returnObject.owner == authentication.name")
    public User getUser(Long id) {
        // 반환된 User 객체의 소유자가 현재 사용자일때만 접근 허용하기
    }
}
