package com.b1uffer.securitymvc.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

    // 더이상 사용되지 않음
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login", "POST");

    private boolean postOnly = true;

    public UsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        if(postOnly && !"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported : " + request.getMethod());
        }

        String username = obtainUsername(request);
        username = username != null ? username.trim() : "";

        String password = obtatinPassword(request);
        password = password != null ? password : "";

        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(username, password);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
    }

    protected String obtatinPassword(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
    }

    protected void setDetails(HttpServletRequest request,
                              UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
