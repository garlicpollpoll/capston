package com.hello.capston.warmup;

import lombok.Data;

@Data
public class WarmupDto {

    private String message;

    public WarmupDto(String message) {
        this.message = message;
    }
}
