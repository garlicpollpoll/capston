package com.hello.capston.jwt.interceptor;

import com.hello.capston.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
public class AddHeaderInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        jwtService.login(email, response);

        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
