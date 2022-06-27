package com.hello.capston.dto.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CouponUploadDto {

    private MultipartFile couponImage;
    private String couponDetail;
    private String percentage;
    private String couponCode;
    private String expireDate;
}
