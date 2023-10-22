package com.hello.capston.entity;

import com.hello.capston.entity.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
public class User implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;

    @Column(nullable = false)
    private String email;

    private String picture;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String roleKey;

    // NEW!
    private String sessionId;

    @Builder
    public User(String name, String email, String picture, Role role, String roleKey, String sessionId) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.roleKey = roleKey;
        this.sessionId = sessionId;
    }

    public User(Long id) {
        this.id = id;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public User updateSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
