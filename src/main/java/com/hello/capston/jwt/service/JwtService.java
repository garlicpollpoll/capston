package com.hello.capston.jwt.service;

import com.hello.capston.jwt.JwtTokenProvider;
import com.hello.capston.jwt.dto.Response;
import com.hello.capston.jwt.dto.UserRequestDto;
import com.hello.capston.jwt.dto.UserResponseDto;
import com.hello.capston.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate redisTemplate;

    public UserResponseDto login(UserRequestDto.Login login) {
        memberRepository.findByLoginId(login.getUsername()).orElseThrow(
                () -> new RuntimeException("Not Found Member")
        );

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이 때 authentication 은 인증 여부를 확인하는 isAuthenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();

        // 2. 실제 검증이 이루어지는 부분
        // authenticate 메서드가 실행될 때 PrincipalDetailService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        UserResponseDto tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 4. RefreshToken Redis 저장
        redisTemplate.opsForValue().set("RT:" + authentication.getName(), tokenInfo, tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        // Customize 5. Spring Security Context Holder 에 Authentication 객체 삽입
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenInfo;
    }
}
