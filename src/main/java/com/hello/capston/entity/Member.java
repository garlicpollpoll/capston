package com.hello.capston.entity;

import com.hello.capston.entity.enums.MemberRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
public class Member implements Serializable {

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

    // NEW!
    private String sessionId;

    public Member(String username, String password, String birth, String gender, MemberRole role, String email, String sessionId) {
        this.username = username;
        this.password = password;
        this.birth = birth;
        this.gender = gender;
        this.role = role;
        this.email = email;
        this.sessionId = sessionId;
    }

    public Member(Long id, String username, String password, String birth, String gender, MemberRole role, String email, String sessionId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.birth = birth;
        this.gender = gender;
        this.role = role;
        this.email = email;
        this.sessionId = sessionId;
    }

    public void changeRoleMemberToManager() {
        this.role = MemberRole.ROLE_MANAGER;
    }

    public void changeRoleManagerToMember() {
        this.role = MemberRole.ROLE_MEMBER;
    }

    public Member update(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }
}
