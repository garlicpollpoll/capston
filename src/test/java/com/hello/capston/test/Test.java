package com.hello.capston.test;

import com.hello.capston.entity.Member;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class Test {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @org.junit.jupiter.api.Test
    public void test() throws Exception {
        //given
        String encode = passwordEncoder.encode("123");
        Member member = new Member("admin", encode, "birth", "gender", MemberRole.ROLE_ADMIN, "email", "session");
        //when
        memberRepository.save(member);

        //then
    }
}
