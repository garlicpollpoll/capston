package com.hello.capston.principal;

import com.hello.capston.entity.Member;
import com.hello.capston.entity.cache.CacheMember;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final CacheRepository cacheRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member principal = memberRepository.findByLoginId(username).orElseThrow(
                () -> new RuntimeException("Not Found Member")
        );
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession();

        session.setAttribute("loginId", principal.getUsername());

        cacheRepository.addMember(principal);

        return new PrincipalDetail(principal);
    }
}
