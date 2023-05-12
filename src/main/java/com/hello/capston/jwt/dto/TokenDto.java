package com.hello.capston.jwt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value = "token", timeToLive = 60 * 30)
public class TokenDto {

    @Id
    private String id;
    private String accessToken;
    private String refreshToken;
    @Indexed
    private String email;

    public TokenDto(String accessToken, String refreshToken, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
    }

    public TokenDto updateToken(String token) {
        this.refreshToken = token;
        return this;
    }
}
