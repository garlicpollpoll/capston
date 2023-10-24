package com.hello.capston.repository.cache;

import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class CacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    public void addMember(Member member) {
        String key = KeyGenerator.memberKeyGenerate(member.getUsername());

        if (!isMemberWasFound(member.getUsername())) {
            redisTemplate.opsForValue().set(key, member);
            redisTemplate.expire(key, 60, TimeUnit.MINUTES);
        }
    }

    public void addUser(Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("Not Found User")
        );
        String key = KeyGenerator.userKeyGenerate(findUser.getEmail());
        if (!isUserWasFound(findUser.getEmail())) {
            redisTemplate.opsForValue().set(key, findUser);
            redisTemplate.expire(key, 60, TimeUnit.MINUTES);
        }
    }

    public Member findMemberAtCache(String loginId) {
        String key = KeyGenerator.memberKeyGenerate(loginId);
        return (Member) redisTemplate.opsForValue().get(key);
    }

    public User findUserAtCache(String email) {
        String key = KeyGenerator.userKeyGenerate(email);
        return (User) redisTemplate.opsForValue().get(key);
    }

    private boolean isMemberWasFound(String loginId) {
        Member findMember = findMemberAtCache(loginId);
        if (findMember != null) {
            return true;
        }
        return false;
    }

    private boolean isUserWasFound(String email) {
        User findUser = findUserAtCache(email);
        if (findUser != null) {
            return true;
        }
        return false;
    }
}
