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
import org.springframework.util.StringUtils;
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
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final RedisTokenRepository redisTokenRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 1. Request Header 에서 JWT 토큰 추출
        String token = resolveToken((HttpServletRequest) request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 2. validateToken 으로 토큰 유효성 검사
        if (token != null  && isValidJwtFormat(token) && jwtTokenProvider.validateToken(token)) {
            // Redis 에 해당 accessToken logout 여부 확인
            String isLogout = (String) redisTemplate.opsForValue().get(token);

            if (ObjectUtils.isEmpty(isLogout)) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                Authentication getAuthentication = jwtTokenProvider.getAuthentication(token);
                HttpSession session = httpRequest.getSession();
                session.setAttribute("loginId", getAuthentication.getName());
                SecurityContextHolder.getContext().setAuthentication(getAuthentication);
            }
        }
        else if (authentication != null && authentication.isAuthenticated()) {  // 3. access token 이 만료된 상황
            // token 은 만료 되었으나 인증이 되어있는 사용자 = 아직 페이지를 안벗어났지만 token 의 유효시간은 끝난 사용자
            UserResponseDto tokenInfo = (UserResponseDto) redisTemplate.opsForValue().get("RT:" + authentication.getName());
            if (tokenInfo != null) {
                setNewAuthentication((HttpServletResponse) response, authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        else { // 4. access token 만료되고 Authentication 객체도 null 인 상황
            String sessionId = getSessionId((HttpServletRequest) request);
            Authentication authenticationFromSessionId = null;
            RedisAndSession redisAndSession = null;
            if (sessionId != null) {
                redisAndSession = redisTokenRepository.findBySessionId(sessionId).orElse(null);
            }

            if (redisAndSession != null) {  // refresh token 이 존재하는 경우 존재하지 않으면 로그인이 풀린다.
                Member findMember = memberRepository.findBySessionId(sessionId).orElse(null);
                User findUser = userRepository.findBySessionId(sessionId).orElse(null);

                if (findMember == null && findUser != null) {   // User 객체만 존재할 경우
                    Collection<? extends GrantedAuthority> authorities =
                            Arrays.stream(findUser.getRole().toString().split(","))
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList());
                    UserDetails principal = new org.springframework.security.core.userdetails.User(findUser.getEmail(), "", authorities);
                    authenticationFromSessionId = new UsernamePasswordAuthenticationToken(principal, "", authorities);
                    setNewAuthentication((HttpServletResponse) response, authenticationFromSessionId);
                    SecurityContextHolder.getContext().setAuthentication(authenticationFromSessionId);
                }
                else if (findUser == null && findMember != null) {  // Member 객체만 존재할 경우
                    Collection<? extends GrantedAuthority> authorities =
                            Arrays.stream(findMember.getRole().toString().split(","))
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList());
                    UserDetails principal = new org.springframework.security.core.userdetails.User(findMember.getUsername(), "", authorities);
                    authenticationFromSessionId = new UsernamePasswordAuthenticationToken(principal, "", authorities);
                    setNewAuthentication((HttpServletResponse) response, authenticationFromSessionId);
                    SecurityContextHolder.getContext().setAuthentication(authenticationFromSessionId);
                }
                else if (findMember != null && findUser != null) {  // 둘 다 존재하는 경우 우선순위는 Member
                    Collection<? extends GrantedAuthority> authorities =
                            Arrays.stream(findMember.getRole().toString().split(","))
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList());
                    UserDetails principal = new org.springframework.security.core.userdetails.User(findMember.getUsername(), "", authorities);
                    authenticationFromSessionId = new UsernamePasswordAuthenticationToken(principal, "", authorities);
                    setNewAuthentication((HttpServletResponse) response, authenticationFromSessionId);
                    SecurityContextHolder.getContext().setAuthentication(authenticationFromSessionId);
                }
                else {  // 둘 다 null 인 경우 에는 로그인이 풀린다.
                    chain.doFilter(request, response);
                }
            }
        }
        chain.doFilter(request, response);
    }

    private void setNewAuthentication(HttpServletResponse response, Authentication authentication) {
        String remakeAccessToken = jwtTokenProvider.remakeAccessToken(authentication);
        Cookie cookie = new Cookie("AUTH-TOKEN", remakeAccessToken);
        ResponseCookie responseCookie =
                ResponseCookie.from("AUTH-TOKEN", remakeAccessToken)
                        .httpOnly(true)
                        .path("/")
                        .maxAge(Duration.ofMinutes(30))
                        .sameSite("Lax")
                        .build();
        response.addCookie(cookie);
        response.addHeader("Set-cookie", responseCookie.toString());
    }

    private String getSessionId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                if (cookieName.equals("SESSION-TOKEN")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
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
