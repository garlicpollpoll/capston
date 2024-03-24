package com.hello.capston.service;

import com.hello.capston.dto.dto.HomeDto;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final CacheRepository cacheRepository;

    public HomeDto homeSetting() {
        String sessionAttribute = null;
        MemberRole role = null;
        String isMemberOrUser = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            UserDetails principal = (UserDetails) authentication.getPrincipal();
            String username = principal.getUsername();

            Member memberAtCache = cacheRepository.findMemberAtCache(username);

            sessionAttribute = memberAtCache.getUsername();
            role = memberAtCache.getRole();
            isMemberOrUser = "loginId";
        }
        return new HomeDto(sessionAttribute, isMemberOrUser, role);
    }
}
