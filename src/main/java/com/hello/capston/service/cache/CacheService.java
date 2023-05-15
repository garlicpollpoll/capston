package com.hello.capston.service.cache;

import com.hello.capston.entity.Member;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheService {

//    private final CacheMemberRepository cacheMemberRepository;
//    private final CacheUserRepository cacheUserRepository;

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
}
