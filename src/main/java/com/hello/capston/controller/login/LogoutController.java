package com.hello.capston.controller.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@Slf4j
public class LogoutController {

    @GetMapping("/custom/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        Cookie cookie = new Cookie("AUTH-TOKEN", null);
        Cookie sessionCookie = new Cookie("SESSION-TOKEN", null);
        log.info("cookie setting");

        cookie.setMaxAge(0);
        cookie.setPath("/");
        log.info("set cookie path");

        sessionCookie.setMaxAge(0);
        sessionCookie.setPath("/");
        log.info("set cookie path");

        response.addCookie(cookie);
        response.addCookie(sessionCookie);
        session.removeAttribute("loginId");
        session.removeAttribute("userEmail");
        log.info("remove cookie and remove session");
        response.sendRedirect("https://www.shopfiesta.kr/");
    }
}
