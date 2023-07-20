package com.hello.capston.test;

import com.hello.capston.entity.Member;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("Username 으로 Member 찾기")
    public void findMemberByUsername() throws Exception {
        //given
        Member member = createMember();
        //when
        memberRepository.save(member);
        Member findMember = memberRepository.findByLoginId(member.getUsername()).orElse(null);
        //then
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    @DisplayName("SessionId 로 Member 찾기")
    public void findMemberBySessionId() throws Exception {
        //given
        Member member = createMember();
        //when
        memberRepository.save(member);
        Member findMember = memberRepository.findBySessionId(member.getSessionId()).orElse(null);
        //then
        Assertions.assertThat(findMember.getSessionId()).isEqualTo(member.getSessionId());
    }

    private Member createMember() {
        String uuid = UUID.randomUUID().toString();
        return new Member("username" + uuid, "password" + uuid, "birth" + uuid, "gender" + uuid, MemberRole.ROLE_MEMBER, "email", "session");
    }
}
