package com.hello.capston.jwt;

import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import com.hello.capston.jwt.dto.UserResponseDto;
import com.hello.capston.jwt.gitbefore.RedisAndSession;
import com.hello.capston.jwt.gitbefore.RedisTokenRepository;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String SESSIONID = "SESSIONID";
    private static final String ACCESS_TOKEN = "AUTH-TOKEN";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 1. Request Header 에서 JWT 토큰 추출
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        boolean isAccessTokenPresent = Arrays.stream(httpRequest.getCookies()).anyMatch((cookie) -> cookie.getName().equals(ACCESS_TOKEN));

        if (isAccessTokenPresent) {
            chain.doFilter(request, response);
        }
        else {
            Cookie sessionCookie = Arrays.stream(httpRequest.getCookies()).filter((cookie) -> cookie.getName().equals(SESSIONID)).findAny().orElseThrow();
            String sessionId = sessionCookie.getValue();

            String refreshToken = (String) redisTemplate.opsForValue().get(sessionId);

            if (isTokenValidate(refreshToken)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                setCookieWithAuthentication(httpResponse, authentication);
            }
        }
        // 2. validateToken 으로 토큰 유효성 검사

        chain.doFilter(request, response);
    }

    private boolean isTokenValidate(String token) {
        return token != null && isValidJwtFormat(token) && jwtTokenProvider.validateToken(token);
    }

    private void setCookieWithAuthentication(HttpServletResponse response, Authentication authentication) {
        String remakeAccessToken = jwtTokenProvider.remakeAccessToken(authentication);
        Cookie cookie = new Cookie(ACCESS_TOKEN, remakeAccessToken);
        ResponseCookie responseCookie =
                ResponseCookie.from(ACCESS_TOKEN, remakeAccessToken)
                        .httpOnly(true)
                        .path("/")
                        .maxAge(Duration.ofMinutes(30))
                        .sameSite("Lax")
                        .build();
        response.addCookie(cookie);
        response.addHeader("Set-cookie", responseCookie.toString());
    }

    private boolean isValidJwtFormat(String token) {
        String[] split = token.split("\\.");
        return split.length == 3;
    }
}
