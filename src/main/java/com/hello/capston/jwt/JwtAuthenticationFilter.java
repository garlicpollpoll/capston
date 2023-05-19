package com.hello.capston.jwt;

import com.hello.capston.jwt.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 1. Request Header 에서 JWT 토큰 추출
        String token = resolveToken((HttpServletRequest) request);

        // 2. validateToken 으로 토큰 유효성 검사
        if (token != null  && isValidJwtFormat(token) && jwtTokenProvider.validateToken(token)) {
            // Redis 에 해당 accessToken logout 여부 확인
            String isLogout = (String) redisTemplate.opsForValue().get(token);

            if (ObjectUtils.isEmpty(isLogout)) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        else {  // 3. access token 이 만료된 상황
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // token 은 만료 되었으나 인증이 되어있는 사용자 = 아직 페이지를 안벗어났지만 token 의 유효시간은 끝난 사용자
            if (authentication != null && authentication.isAuthenticated()) {
                UserResponseDto tokenInfo = (UserResponseDto) redisTemplate.opsForValue().get("RT:" + authentication.getName());
                if (tokenInfo != null) {
                    setNewAuthentication((HttpServletResponse) response, authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }

    private void setNewAuthentication(HttpServletResponse response, Authentication authentication) {
        // TODO subject 추후에 바꿔줘야 함
        String remakeRefreshToken = jwtTokenProvider.remakeRefreshToken(authentication);
        Cookie cookie = new Cookie("AUTH-TOKEN", remakeRefreshToken);
        ResponseCookie responseCookie =
                ResponseCookie.from("AUTH-TOKEN", remakeRefreshToken)
                        .httpOnly(true)
                        .path("/")
                        .maxAge(Duration.ofMinutes(30))
                        .sameSite("Lax")
                        .build();
        response.addCookie(cookie);
        response.addHeader("Set-cookie", responseCookie.toString());
    }

    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                if (cookieName.equals("AUTH-TOKEN")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private boolean isValidJwtFormat(String token) {
        String[] split = token.split("\\.");
        return split.length == 3;
    }
}
