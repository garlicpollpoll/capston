package com.hello.capston.absctracts.policy.config;

import com.hello.capston.absctracts.policy.Policy;
import com.hello.capston.absctracts.policy.impl.MemberPolicy;
import com.hello.capston.absctracts.policy.impl.UserPolicy;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.MemberWhoGetCouponRepository;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PolicyManager {

    private final CacheRepository cacheRepository;
    private final MemberWhoGetCouponRepository memberWhoGetCouponRepository;

    public Policy policy(MemberRole role) {
        if (role.equals(MemberRole.ROLE_SOCIAL)) {
            return new UserPolicy(cacheRepository, memberWhoGetCouponRepository);
        }
        else {
            return new MemberPolicy(cacheRepository, memberWhoGetCouponRepository);
        }
    }

}
