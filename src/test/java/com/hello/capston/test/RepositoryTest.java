package com.hello.capston.test;

import com.hello.capston.entity.Member;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class RepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void test() throws Exception {
        //given
        Member member = new Member("username", "password", "birth", "gender", MemberRole.ROLE_MEMBER, "email", "session");
        //when
        memberRepository.save(member);
        Member findMember = memberRepository.findByLoginId(member.getUsername()).orElse(null);
        //then
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }
}
