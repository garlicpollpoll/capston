package com.hello.capston.repository;

import com.hello.capston.entity.Member;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.cache.CacheRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisCachingTest {

    @Autowired
    CacheRepository cacheRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void cacheTest() throws Exception {
        //given
        Member findMember = cacheRepository.findMemberAtCache("ks3254");
        //when

        //then
        System.out.println(findMember.getUsername());
        System.out.println(findMember.getEmail());
    }

    @Test
    public void addCacheTest() throws Exception {
        //given
        Member member = new Member("username", "password", "981007", "Man", MemberRole.ROLE_MEMBER, "k@k.com", "sessionId");
        //when
        Member saveMember = memberRepository.save(member);
        cacheRepository.addMember(saveMember);
        //then
    }

}
