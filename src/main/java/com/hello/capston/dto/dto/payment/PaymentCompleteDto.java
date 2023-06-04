package com.hello.capston.dto.dto.payment;

import lombok.Data;

@Data
public class PaymentCompleteDto {

    private String memberId;
    private String userId;
    private String zipcode;
    private String street;
    private String detailAddress;
    private String target;

    public PaymentCompleteDto(String memberId, String userId, String zipcode, String street, String detailAddress, String target) {
        this.memberId = memberId;
        this.userId = userId;
        this.zipcode = zipcode;
        this.street = street;
        this.detailAddress = detailAddress;
        this.target = target;
    }

    public PaymentCompleteDto() {
    }
}
