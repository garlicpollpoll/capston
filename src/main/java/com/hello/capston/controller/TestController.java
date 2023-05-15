package com.hello.capston.controller;

import com.hello.capston.entity.Member;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final CacheRepository cacheRepository;

    @GetMapping("/test")
    public String test(HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        Member findMember = cacheRepository.findMemberAtCache(loginId);

        return findMember.getUsername();
    }
}
