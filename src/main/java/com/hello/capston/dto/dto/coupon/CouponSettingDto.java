package com.hello.capston.dto.dto.coupon;

import com.hello.capston.entity.Coupon;
import lombok.Data;

@Data
public class CouponSettingDto {

    private String username;
    private String code;
    private Coupon coupon;
}
