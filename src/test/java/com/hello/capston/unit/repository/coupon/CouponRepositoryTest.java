package com.hello.capston.unit.repository.coupon;

import com.hello.capston.entity.Coupon;
import com.hello.capston.repository.CouponRepository;
import com.hello.capston.unit.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CouponRepositoryTest {

    @Autowired
    CouponRepository couponRepository;

    @Test
    public void test() throws Exception {
        //given
        Coupon coupon = Data.createCoupon();
        //when
        Coupon saveCoupon = couponRepository.save(coupon);

        Coupon findCoupon = couponRepository.findByCode(saveCoupon.getCode()).orElse(null);
        //then
        Assertions.assertNotNull(findCoupon);
        Assertions.assertEquals(coupon.getPercentage(), findCoupon.getPercentage());
    }

    @Test
    @DisplayName("쿠폰 디테일 (쿠폰의 설명) (으)로 쿠폰 찾기")
    public void test2() throws Exception {
        //given
        Coupon coupon = Data.createCoupon();
        //when
        Coupon saveCoupon = couponRepository.save(coupon);

        Coupon findCoupon = couponRepository.findByDetail(saveCoupon.getCouponDetail()).orElse(null);
        //then
        Assertions.assertNotNull(findCoupon);
        Assertions.assertEquals(coupon.getCouponDetail(), findCoupon.getCouponDetail());
    }
}
