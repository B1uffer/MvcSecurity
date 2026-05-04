package com.b1uffer.securitymvc.custom;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class CspNonceFilter extends OncePerRequestFilter {
    private final SecureRandom random = new SecureRandom();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 요청 전 처리 로직
        byte[] b = new byte[16];
        random.nextBytes(b);

        String nonce = Base64.getUrlEncoder().encodeToString(b);
        request.setAttribute("cspNonce", nonce); // nonce 생성
        // 헤더 주입
        response.setHeader("Content-Security-Policy",
                "default-src 'self'; script-src 'nonce-" + nonce + "' " +
                        "'strict-dynamic'; object-src 'none' base-uri 'self'; frame-ancestors 'none'"
        );

        // 필터체인 진행
        filterChain.doFilter(request, response);
    }

    /**
     * 이 체인을 h2-console 및 특정 uri에서 돌아가지 않게끔 하는 메서드
     * 혹은 @Component를 빼고 SecurityConfig의 원하는 체인에 securityMatcher로 해당 클래스를 넣으면 된다
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/h2-console");
    }
}
