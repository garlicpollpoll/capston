package com.hello.capston.jwt.repository;

import com.hello.capston.jwt.JwtUtil;
import com.hello.capston.jwt.dto.TokenDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RefreshTokenRepositoryTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    public void test() throws Exception {
        //given
        Optional<TokenDto> byEmail = refreshTokenRepository.findByEmail("kyoungsuk3254@naver.com");
        //when
        if (byEmail.isPresent()) {
            System.out.println("success");
        }
        else {
            System.out.println("not success");
        }
        //then
    }

}