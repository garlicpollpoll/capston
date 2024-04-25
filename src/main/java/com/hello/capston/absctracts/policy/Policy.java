package com.hello.capston.absctracts.policy;


import com.hello.capston.dto.dto.CouponDto;
import com.hello.capston.dto.dto.coupon.CouponSettingDto;

import java.util.Map;

public interface Policy {

    CouponDto isCoupon(CouponSettingDto dto);

    Map<String, Double> selectCoupon();
}
