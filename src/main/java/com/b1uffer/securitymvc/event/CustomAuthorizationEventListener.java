package com.b1uffer.securitymvc.event;

import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationEvent;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthorizationEventListener {
    @EventListener
    public void handleAuthEvent(AuthorizationEvent event) {
        System.out.println("인가 이벤트 발생 : " + event);
    }
}
