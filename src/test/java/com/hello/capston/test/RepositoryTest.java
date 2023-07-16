package com.hello.capston.test;

import com.hello.capston.entity.Member;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest
@Transactional
public class RepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void test() throws Exception {
        //given
        String uuid = createUUID();
        Member member = new Member("username", "password", "birth", "gender", MemberRole.ROLE_MEMBER, "email", uuid);
        //when
        memberRepository.save(member);
        Member findMember = memberRepository.findByLoginId(member.getUsername()).orElse(null);
        //then
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    public void test2() throws Exception {
        //given
        Member member = createMember();
        //when
        memberRepository.save(member);
        Member findMemberBySession = memberRepository.findBySessionId(member.getSessionId()).orElse(null);
        //then
        Assertions.assertThat(findMemberBySession.getUsername()).isEqualTo(member.getUsername());
    }

    private Member createMember() {
        return new Member("username", "password", "birth", "gender", MemberRole.ROLE_MEMBER, "email", createUUID());
    }

    private String createUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}