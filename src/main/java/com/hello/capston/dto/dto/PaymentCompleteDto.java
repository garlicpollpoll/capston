package com.hello.capston.dto.dto;

import lombok.Data;

@Data
public class PaymentCompleteDto {

    private String memberId;
    private String userId;
    private String zipcode;
    private String street;
    private String detailAddress;

    public PaymentCompleteDto(String memberId, String userId, String zipcode, String street, String detailAddress) {
        this.memberId = memberId;
        this.userId = userId;
        this.zipcode = zipcode;
        this.street = street;
        this.detailAddress = detailAddress;
    }

    public PaymentCompleteDto() {
    }
}
