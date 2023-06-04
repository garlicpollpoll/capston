package com.hello.capston.unit.repository.member;

import com.hello.capston.entity.Member;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.unit.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("Login Id 로 Member 찾기")
    public void test() throws Exception {
        //given
        Member member = Data.createMember();
        //when
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findByLoginId(saveMember.getUsername()).orElse(null);
        //then
        Assertions.assertNotNull(findMember);
        Assertions.assertEquals(member.getUsername(), findMember.getUsername());
    }

    @Test
    @DisplayName("Session Id 로 Member 찾기")
    public void test2() throws Exception {
        //given
        Member member = Data.createMember();
        //when
        Member saveMember = memberRepository.save(member);
        Member findMember = memberRepository.findBySessionId(saveMember.getSessionId()).orElse(null);
        //then
        Assertions.assertNotNull(findMember);
        Assertions.assertEquals(member.getUsername(), findMember.getUsername());
    }
}
