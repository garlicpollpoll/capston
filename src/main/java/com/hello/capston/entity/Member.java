package com.hello.capston.entity;

import com.hello.capston.entity.enums.MemberRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;

    private String birth;
    private String gender;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String email;

    public Member(String username, String password, String birth, String gender, MemberRole role, String email) {
        this.username = username;
        this.password = password;
        this.birth = birth;
        this.gender = gender;
        this.role = role;
        this.email = email;
    }

    public void changeRoleMemberToManager() {
        this.role = MemberRole.ROLE_MANAGER;
    }

    public void changeRoleManagerToMember() {
        this.role = MemberRole.ROLE_MEMBER;
    }
}
