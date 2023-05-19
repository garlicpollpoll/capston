package com.hello.capston.controller.login;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {

    @GetMapping("/custom/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        Cookie cookie = new Cookie("AUTH-TOKEN", null);
        Cookie sessionCookie = new Cookie("SESSION-TOKEN", null);

        cookie.setMaxAge(0);
        cookie.setPath("/");

        sessionCookie.setMaxAge(0);
        sessionCookie.setPath("/");

        response.addCookie(cookie);
        response.addCookie(sessionCookie);
        session.removeAttribute("loginId");
        session.removeAttribute("userEmail");
        return "redirect:/";
    }
}
