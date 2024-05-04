package com.hello.capston.absctracts.policy;


import com.hello.capston.dto.dto.coupon.CouponDto;
import com.hello.capston.dto.request.SelectCouponDto;
import com.hello.capston.dto.dto.coupon.CouponSettingDto;

import java.util.Map;

public interface CouponPolicy {

    CouponDto isCoupon(CouponSettingDto dto);

    Map<String, Double> selectCoupon(String username, SelectCouponDto dto);
}
