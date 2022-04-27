package com.hello.capston.service;

import com.hello.capston.entity.Member;
import com.hello.capston.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findMember(String loginId) {
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
