package com.hello.capston.jwt;

import com.hello.capston.entity.User;
import com.hello.capston.entity.enums.Role;
import com.hello.capston.jwt.dto.UserResponseDto;
import com.hello.capston.repository.token.RedisTokenRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class JwtRedisTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisTokenRepository redisTokenRepository;

    @Test
    @DisplayName("키가 아닌 값으로 찾으면 null 이 나와야한다.")
    public void test1() throws Exception {
        //given
        Authentication authentication = getAuthentication();
        UserResponseDto tokenInfo = jwtTokenProvider.generateToken(authentication);
        //when
        redisTemplate.opsForValue().set("RT:" + authentication.getName(), tokenInfo, 5, TimeUnit.MINUTES);
        UserResponseDto result = (UserResponseDto) redisTemplate.opsForValue().get(tokenInfo.getRefreshToken());
        //then
        Assertions.assertThat(result).isEqualTo(null);
    }

    @Test
    @DisplayName("키로 찾으면 값이 나와야 한다.")
    public void test2() throws Exception {
        //given
        Authentication authentication = getAuthentication();
        UserResponseDto tokenInfo = jwtTokenProvider.generateToken(authentication);
        //when
        redisTemplate.opsForValue().set("RT:" + authentication.getName(), tokenInfo, 30, TimeUnit.SECONDS);
        UserResponseDto result = (UserResponseDto) redisTemplate.opsForValue().get("RT:" + authentication.getName());
        //then
        Assertions.assertThat(tokenInfo.getRefreshToken()).isEqualTo(result.getRefreshToken());
    }

    private Authentication getAuthentication() {
        User user = new User("임경석", "kyoungsuk3254@naver.com", "picture", Role.GUEST, Role.GUEST.getKey());
        return new UsernamePasswordAuthenticationToken(user, "password");
    }

    private Authentication getAuthentication1() {
        User user = new User("임경석", "kyoungsuk3254@naver.com", "picture", Role.GUEST, Role.GUEST.getKey());
        return new UsernamePasswordAuthenticationToken(user, "password");
    }
}
