package com.b1uffer.securitymvc.config;

import com.b1uffer.securitymvc.custom.CustomGenericFilter;
import com.b1uffer.securitymvc.custom.CustomOncePerRequestFilter;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.nio.file.Path;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http    .securityMatcher("/api/secure/**") // 해당 체인이 이 URL만으로 처리된다
                .addFilterBefore(new CustomOncePerRequestFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new CustomGenericFilter(), CsrfFilter.class)
                .addFilterAt(new CustomOncePerRequestFilter(), BasicAuthenticationFilter.class)
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

    @Bean
    @Order(2)
    public SecurityFilterChain xssFilterChain(HttpSecurity http) throws Exception {
        http.headers(headers -> headers
                .contentTypeOptions(Customizer.withDefaults()) // X-Content-Type-Options : nosniff
                .contentSecurityPolicy(csp -> csp.policyDirectives(
                        "default-src 'self'; " +
                        "script-src 'self' 'nonce-{{nonce}}' 'strict-dynamic'; " +
                        "object-src 'none'; base-uri 'self'; frame-ancestors 'none'"
                ))
                // .xssProtection(x -> x.block(true))는 레거시 코드로, 최신 브라우저에서는 대부분 무시된다
        );
        return http.build();
    }

    /**
     * h2 Console 전용 체인
     */
    @Bean
    @Order(0)
    public SecurityFilterChain h2ConsoleFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(PathRequest.toH2Console())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                // XSS
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "script-src 'self' 'unsafe-inline'; " +
                                        "object-src 'none'; " +
                                        "frame-ancestors 'self'"
                        ))
                );

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

//    @PreAuthorize("hasRole('ADMIN')")
//    public void deleteUser(Long id) {
//        // ADMIN 권한을 가진 사용자만 실행 가능한 메서드
//    }
//
//    @PostAuthorize("returnObject.owner == authentication.name")
//    public User getUser(Long id) {
//        // 반환된 User 객체의 소유자가 현재 사용자일때만 접근 허용하기
//    }
}
