package com.hello.capston.jwt;

import com.hello.capston.jwt.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserResponseDto tokenInfo = jwtTokenProvider.generateToken(authentication);

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
        response.setHeader("Set-cookie", responseCookie.toString());
        response.sendRedirect("/");
    }
}
