package com.b1uffer.securitymvc.auth;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventListener {
    @EventListener
    public void handleSuccess(AuthenticationSuccessEvent event) {
        System.out.println("로그인 성공 : " + event.getAuthentication().getName());
    }

    @EventListener
    public void handleFailure(AuthenticationFailureBadCredentialsEvent event) {
        System.out.println("로그인 실패 : " + event.getException().getMessage());
    }
}
