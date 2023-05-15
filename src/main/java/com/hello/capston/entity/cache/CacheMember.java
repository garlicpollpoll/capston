package com.hello.capston.entity.cache;

import com.hello.capston.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "cache_member", timeToLive = 30 * 60)
@Getter
@NoArgsConstructor
public class CacheMember {

    @Id
    private String id;
    private Member member;

    public CacheMember(Member member) {
        this.member = member;
    }
}
