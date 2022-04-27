package com.hello.capston.dto.dto;

import lombok.Data;

@Data
public class CancelDto {

    private String imp_uid;
    private String amount;
    private String checksum;
    private String reason;
}
