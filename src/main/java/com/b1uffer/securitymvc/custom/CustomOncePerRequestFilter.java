package com.b1uffer.securitymvc.custom;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CustomOncePerRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        /**
         * 요청 전 처리
         * 클라이언트의 IP를 가져온다
         * OS 레벨의 TCP 이후 tomcat을 통해 IP를 가져옴
         */
        String clientIp = request.getRemoteAddr();
        System.out.println("[CustomOncePerRequestFilter] 클라이언트 IP : " + clientIp);

        // 필터 체인 진행
        filterChain.doFilter(request, response);

        // 응답 후 처리
        System.out.println("[CustomOncePerRequestFilter] 응답 완료");
    }
}
