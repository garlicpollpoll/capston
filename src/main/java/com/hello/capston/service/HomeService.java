package com.hello.capston.service;

import com.hello.capston.dto.dto.HomeDto;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final MemberRepository memberRepository;

    public HomeDto homeSetting() {
        String sessionAttribute = null;
        MemberRole role = null;
        String isMemberOrUser = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            String name = authentication.getName();

            if (name != "anonymousUser") {
                Member findMemberFromAuthentication = memberRepository.findByLoginId(name).orElse(null);
                if (findMemberFromAuthentication != null) {
                    sessionAttribute = findMemberFromAuthentication.getUsername();
                    role = findMemberFromAuthentication.getRole();
                    isMemberOrUser = "loginId";
                }
//                else {
//                    User principal = (User) authentication.getPrincipal();
//                    sessionAttribute = principal.getEmail();
//                    isMemberOrUser = "userEmail";
//                }
            }
        }
        return new HomeDto(sessionAttribute, isMemberOrUser, role);
    }
}
