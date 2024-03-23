package com.hello.capston.service;

import com.hello.capston.entity.enums.MemberRole;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class WhatIsRoleService {

    public MemberRole whatIsRole(Authentication authentication) {
        boolean isRoleMember = authentication.getAuthorities().stream().anyMatch(role -> role.equals(MemberRole.ROLE_MEMBER));
        boolean isRoleSocial = authentication.getAuthorities().stream().anyMatch(role -> role.equals(MemberRole.ROLE_SOCIAL));

        if (isRoleMember) {
            return MemberRole.ROLE_MEMBER;
        }
        else if (isRoleSocial) {
            return MemberRole.ROLE_SOCIAL;
        }
        return MemberRole.ROLE_MEMBER;
    }
}
