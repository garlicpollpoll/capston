package com.hello.capston.warmup;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ReadinessInterceptor implements HandlerInterceptor {

    private boolean isReady = false;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!isReady && !request.getRequestURI().equals("/warmup/complete")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Application is not ready yet");
            return false;
        }
        return true;
    }

    public void setReady(boolean ready) {
        this.isReady = ready;
    }
}