package com.hello.capston.jwt;

import com.hello.capston.entity.User;
import com.hello.capston.jwt.dto.UserResponseDto;
import com.hello.capston.jwt.gitbefore.RedisAndSession;
import com.hello.capston.jwt.gitbefore.RedisTokenRepository;
import com.hello.capston.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final RedisTokenRepository redisTokenRepository;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserResponseDto tokenInfo = jwtTokenProvider.generateToken(authentication);

        // NEW!
        String sessionId = request.getSession(true).getId();
        RedisAndSession redisAndSession = RedisAndSession.builder().sessionId(sessionId).refreshToken(tokenInfo.getRefreshToken()).build();
        redisTokenRepository.save(redisAndSession);
        Cookie sessionCookie = new Cookie("SESSION-TOKEN", sessionId);
        sessionCookie.setHttpOnly(true);
        ResponseCookie responseSessionCookie = ResponseCookie
                .from("SESSION-TOKEN", sessionId)
                .maxAge(Duration.ofDays(7))
                .httpOnly(true)
                .path("/")
                .sameSite("Lax")
                .build();
        response.setHeader("Set-Cookie", responseSessionCookie.toString());
        response.addCookie(sessionCookie);
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email");
        User user = userRepository.findByEmail(email).map(entity -> entity.updateSessionId(sessionId)).orElse(principal);
        userRepository.save(user);
        // NEW!

        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo, tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Cookie cookie = new Cookie("AUTH-TOKEN", tokenInfo.getAccessToken());
        cookie.setHttpOnly(true);

        ResponseCookie responseCookie =
                ResponseCookie.from("AUTH-TOKEN", tokenInfo.getAccessToken())
                        .sameSite("Lax")
                        .httpOnly(true)
                        .maxAge(Duration.ofMinutes(30))
                        .path("/")
                        .build();

        response.addCookie(cookie);
        response.setHeader("Set-Cookie", responseCookie.toString());
        response.sendRedirect("/");
    }
}
