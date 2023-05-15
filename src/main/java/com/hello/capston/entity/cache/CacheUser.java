package com.hello.capston.entity.cache;

import com.hello.capston.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "cache_user", timeToLive = 30 * 60)
@Getter
@NoArgsConstructor
public class CacheUser {

    @Id
    private String id;
    private User user;

    public CacheUser(User user) {
        this.user = user;
    }
}
