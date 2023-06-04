package com.hello.capston.dto.dto.payment;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PaymentDto {

    @NotEmpty
    private String recipient;   //수령인
    @NotEmpty
    private String first;       //010
    @NotEmpty
    private String second;      //****
    @NotEmpty
    private String third;       //****
    @NotEmpty
    private String zipcode;     //우편번호
    @NotEmpty
    private String street;      //도로명주소
    @NotEmpty
    private String detailAddress;   //상세주소
    @NotEmpty
    private String memo;        //배송메모

}
