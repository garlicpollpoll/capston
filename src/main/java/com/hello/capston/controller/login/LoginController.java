package com.hello.capston.controller.login;

import com.hello.capston.dto.form.LoginForm;
import com.hello.capston.entity.Member;
import com.hello.capston.jwt.dto.JwtAuthenticationResponse;
import com.hello.capston.jwt.dto.UserRequestDto;
import com.hello.capston.jwt.dto.UserResponseDto;
import com.hello.capston.jwt.gitbefore.RedisAndSession;
import com.hello.capston.jwt.gitbefore.RedisTokenRepository;
import com.hello.capston.jwt.service.JwtService;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final CacheRepository cacheRepository;
    private final JwtService jwtService;
    private final RedisTokenRepository redisTokenRepository;
    private final MemberRepository memberRepository;

    /**
     * 로그인 페이지
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        LoginForm form = new LoginForm();

        String loginId = (String) session.getAttribute("loginId");

        Member findMember = cacheRepository.findMemberAtCache(loginId);

        if (findMember != null) {
            model.addAttribute("status", findMember.getRole());
        }

        model.addAttribute("login", form);

        return "login";
    }

    @ResponseBody
    @PostMapping("/custom/login")
    public ResponseEntity<?> login_post(@Validated @RequestBody LoginForm form, BindingResult bindingResult, HttpServletResponse response,
                                        HttpServletRequest request) {
        UserRequestDto.Login login = new UserRequestDto.Login();
        login.setUsername(form.getLoginId());
        login.setPassword(form.getLoginPw());

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아이디와 비밀번호는 빈칸일 수 없습니다.");
        }

        UserResponseDto tokenInfo = jwtService.login(login);

        Cookie authCookie = new Cookie("AUTH-TOKEN", tokenInfo.getAccessToken());

        authCookie.setHttpOnly(true);

        response.addCookie(authCookie);

        ResponseCookie cookie =
                ResponseCookie.from("AUTH-TOKEN", tokenInfo.getAccessToken())
                        .sameSite("Lax")
                        .httpOnly(true)
                        .secure(false)
                        .path("/")
                        .maxAge(Duration.ofMinutes(30))
                        .build();

        response.addHeader("Set-Cookie", cookie.toString());

        // NEW!
        String uuid = UUID.randomUUID().toString();
        Cookie sessionCookie = new Cookie("SESSIONID", uuid);
        sessionCookie.setHttpOnly(true);
        response.addCookie(sessionCookie);
        ResponseCookie responseSessionCookie =
                ResponseCookie.from("SESSIONID", uuid)
                        .sameSite("Lax")
                        .httpOnly(true)
                        .secure(false)
                        .path("/")
                        .maxAge(Duration.ofDays(7))
                        .build();
        response.addHeader("Set-Cookie", responseSessionCookie.toString());
        //NEW!

        return ResponseEntity.ok(new JwtAuthenticationResponse(tokenInfo.getAccessToken()));
    }

    @GetMapping("/protected-endpoint")
    @ResponseBody
    public String endpoint(HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
        return "success";
    }
}
