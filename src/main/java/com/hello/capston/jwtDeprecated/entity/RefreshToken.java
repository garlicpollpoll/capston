package com.hello.capston.jwtDeprecated.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    private String id;

    @NotBlank
    private String refreshToken;

    @NotBlank
    private String accountEmail;

    public RefreshToken(String token, String email) {
        this.refreshToken = token;
        this.accountEmail = email;
    }

    public RefreshToken updateToken(String token) {
        this.refreshToken = token;
        return this;
    }
}
