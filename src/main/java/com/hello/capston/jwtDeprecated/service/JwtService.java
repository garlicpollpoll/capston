package com.hello.capston.jwtDeprecated.service;

import com.hello.capston.jwtDeprecated.JwtUtil;
import com.hello.capston.jwtDeprecated.dto.GlobalResDto;
import com.hello.capston.jwtDeprecated.dto.TokenDto;
import com.hello.capston.jwtDeprecated.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public GlobalResDto login(String email, HttpServletResponse response) {
        // 아이디 정보로 token 생성
        TokenDto token = jwtUtil.createAllToken(email);

        // refresh token 있는지 확인
        Optional<TokenDto> refreshToken = refreshTokenRepository.findByEmail(token.getEmail());

        // 있다면 새 토큰 발급 후 업데이트
        // 없다면 새로 만들고 디비 저장
        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(token.getRefreshToken()));
        }
        else {
            TokenDto tokenDto = new TokenDto(token.getAccessToken(), token.getRefreshToken(), token.getEmail());
            refreshTokenRepository.save(tokenDto);
        }

        // response 헤더에 Access Token / Refresh Token 넣음
        setHeader(response, token);

        return new GlobalResDto("Success Login", HttpStatus.OK.value());
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}
