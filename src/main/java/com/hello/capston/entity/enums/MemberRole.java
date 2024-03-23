package com.hello.capston.entity.enums;

import lombok.Getter;

@Getter
public enum MemberRole {

    ROLE_ADMIN("관리자"), ROLE_MANAGER("매니저"), ROLE_MEMBER("일반 로그인 사용자"), ROLE_SOCIAL("소셜 로그인 사용자");

    private String description;

    MemberRole(String description) {
        this.description = description;
    }
}
