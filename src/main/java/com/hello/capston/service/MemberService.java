package com.hello.capston.service;

import com.hello.capston.entity.Member;
import com.hello.capston.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findMember(String loginId) {
        log.info("MemberService 접속했음");
        return memberRepository.findByLoginId(loginId).orElse(null);
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    public boolean findByLoginId(String loginId) {
        Member findMember = memberRepository.findByLoginId(loginId).orElse(null);

        if (findMember == null) {
            return true;
        }
        else {
            return false;
        }
    }
}
