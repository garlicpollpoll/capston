package com.hello.capston.jwt.gitbefore;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value = "token", timeToLive = 7 * 24 * 60 * 60)
@Builder
public class RedisAndSession {

    @Id
    private String id;
    @Indexed
    private String sessionId;
    private String refreshToken;

    public RedisAndSession(String id, String sessionId, String refreshToken) {
        this.id = id;
        this.sessionId = sessionId;
        this.refreshToken = refreshToken;
    }
}
