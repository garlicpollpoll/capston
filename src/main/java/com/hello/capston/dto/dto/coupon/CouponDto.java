package com.hello.capston.dto.dto.coupon;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CouponDto {

    private Map<String, String> map = new HashMap<>();
    private boolean isCoupon;

    public CouponDto(Map<String, String> map, boolean isCoupon) {
        this.map = map;
        this.isCoupon = isCoupon;
    }
}
