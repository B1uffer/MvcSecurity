package com.b1uffer.securitymvc.custom;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomGenericFilter extends GenericFilterBean { // GenericFilterBean을 extends 한 다음
    // doFilter 메서드만 구현하면 됨
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        // 요청 전 처리 로직
        System.out.println("[CustomGenericFilter] 요청 처리 전 실행하기");

        // 필터 체인 진행
        filterChain.doFilter(servletRequest, servletResponse);

        // 응답 후 처리 로직
        System.out.println("[CustomGenericFilter] 응답 처리 후 실행하기");
    }
}
