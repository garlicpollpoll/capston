package com.hello.capston.listener;

import com.hello.capston.entity.Member;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheExpireListener implements MessageListener {

    private final CacheRepository cacheRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Member principal = (Member) authentication.getPrincipal();
            String username = principal.getUsername();
            Member memberAtCache = cacheRepository.findMemberAtCache(username);
            cacheRepository.addMember(memberAtCache);
        }
    }
}
