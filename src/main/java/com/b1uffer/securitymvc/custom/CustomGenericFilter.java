package com.b1uffer.securitymvc.custom;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

public class CustomGenericFilter extends GenericFilterBean { // GenericFilterBean을 extends 한 다음
    // doFilter 메서드만 구현하면 됨
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        // 요청 전 처리 로직
        System.out.println("[CustomGenericFilter] 요청 처리 전 실행하기");

        // 전처리
        String clientIp = servletRequest.getRemoteAddr();
        List<String> whiteList = List.of("127.0.0.1", "192.168.0.10");
        if(!whiteList.contains(clientIp)) {
            servletResponse.setContentType("text/plain;charset=utf-8");
            servletResponse.getWriter().write("허용되지 않은 IP입니다.");
            return; // 꼭 해줘야함, 필터 체인 중단
        }

        // 필터 체인 진행
        filterChain.doFilter(servletRequest, servletResponse);

        // 응답 후 처리 로직
        System.out.println("[CustomGenericFilter] 응답 처리 후 실행하기");
    }
}
