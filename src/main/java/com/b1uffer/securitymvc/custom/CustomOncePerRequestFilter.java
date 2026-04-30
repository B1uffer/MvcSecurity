package com.b1uffer.securitymvc.custom;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

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

        String token = request.getHeader("Authorization");
        if(token == null && token.startsWith("Bearer ")) {
            // 토큰 파싱 및 검증 로직 구현
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return; // 인증에 실패하면 필터체인이 중단됨

            // 검증에 성공하면 Authentication 객체 생성 후 SecurityContext에 저장하는 로직
        }
        String traceId = UUID.randomUUID().toString();
        response.addHeader("X-Trace-Id", traceId);
        System.out.println("[Trace] 요청 추적 ID : " + traceId);

        // 필터 체인 진행
        filterChain.doFilter(request, response);

        // 응답 후 처리
        System.out.println("[CustomOncePerRequestFilter] 응답 완료");
    }
}
