package com.hello.capston.jwtDeprecated.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GlobalResDto {

    private String message;
    private int statusCode;

    public GlobalResDto(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
