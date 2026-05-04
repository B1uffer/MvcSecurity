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
}
