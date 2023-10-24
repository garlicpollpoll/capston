package com.hello.capston.service;

import com.hello.capston.dto.dto.CouponDto;
import com.hello.capston.dto.dto.SelectCouponDto;
import com.hello.capston.entity.*;
import com.hello.capston.repository.CouponRepository;
import com.hello.capston.repository.MemberWhoGetCouponRepository;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CacheRepository cacheRepository;
    private final MemberWhoGetCouponRepository memberWhoGetCouponRepository;
    private final CouponRepository couponRepository;

    private final TemporaryOrderService temporaryOrderService;


    public CouponDto isCoupon(String loginId, String userEmail, String code) {
        Map<String, String> map = new HashMap<>();
        Coupon coupon = couponRepository.findByCode(code).orElse(null);
        boolean isCouponHas = false;

        if (coupon == null) {
            map.put("message", "존재하지 않는 쿠폰입니다.");
            map.put("url", "/coupon");
            return new CouponDto(map, isCouponHas);
        }

        if (loginId == null) {
            User findUser = cacheRepository.findUserAtCache(userEmail);
            List<MemberWhoGetCoupon> findCoupon = memberWhoGetCouponRepository.findCouponByUserId(findUser.getId());

            if (findCoupon.isEmpty()) {
                memberWhoGetCouponRepository.save(new MemberWhoGetCoupon(findUser, null, coupon, 0));
                map.put("message", "쿠폰이 등록되었습니다.");
                map.put("url", "/");
                return new CouponDto(map, isCouponHas);
            }

            for (MemberWhoGetCoupon memberWhoGetCoupon : findCoupon) {
                if (memberWhoGetCoupon.getCoupon().getCode().equals(code)) {
                    map.put("message", "이미 가지고 있는 쿠폰입니다.");
                    map.put("url", "/coupon");
                    isCouponHas = true;
                    return new CouponDto(map, isCouponHas);
                }
                else {
                    memberWhoGetCouponRepository.save(new MemberWhoGetCoupon(findUser, null, coupon, 0));
                    map.put("message", "쿠폰이 등록되었습니다.");
                    map.put("url", "/");
                    return new CouponDto(map, isCouponHas);
                }
            }
        }

        if (userEmail == null) {
            Member findMember = cacheRepository.findMemberAtCache(loginId);
            List<MemberWhoGetCoupon> findCoupon = memberWhoGetCouponRepository.findCouponByMemberId(findMember.getId());

            if (findCoupon.isEmpty()) {
                memberWhoGetCouponRepository.save(new MemberWhoGetCoupon(null, findMember, coupon, 0));
                map.put("message", "쿠폰이 등록되었습니다.");
                map.put("url", "/");
                return new CouponDto(map, isCouponHas);
            }

            for (MemberWhoGetCoupon memberWhoGetCoupon : findCoupon) {
                if (memberWhoGetCoupon.getCoupon().getCode().equals(code)) {
                    map.put("message", "이미 가지고 있는 쿠폰입니다.");
                    map.put("url", "/coupon");
                    isCouponHas = true;
                    return new CouponDto(map, isCouponHas);
                }
                else {
                    memberWhoGetCouponRepository.save(new MemberWhoGetCoupon(null, findMember, coupon, 0));
                    map.put("message", "쿠폰이 등록되었습니다.");
                    map.put("url", "/");
                    return new CouponDto(map, isCouponHas);
                }
            }
        }

        return new CouponDto(map, isCouponHas);
    }

    public Map<String, Double> selectCoupon(String loginId, String userEmail, SelectCouponDto dto) {
        Map<String, Double> map = new HashMap<>();
        int totalPrice = 0;
        double percentage = 0;

        if (loginId == null && userEmail != null) {
            User findUser = cacheRepository.findUserAtCache(userEmail);
            List<TemporaryOrder> findTOrder = temporaryOrderService.findTOrderListByUserId(findUser.getId());

            for (TemporaryOrder temporaryOrder : findTOrder) {
                totalPrice += temporaryOrder.getPrice() * temporaryOrder.getCount();
            }

            Coupon coupon = couponRepository.findByDetail(dto.getTarget()).orElse(null);

            percentage = 1 - (coupon.getPercentage() * 0.01);

            map.put("orderPrice", totalPrice * percentage);
            map.put("discountPrice", totalPrice - (totalPrice * percentage));
        }

        if (userEmail == null && loginId != null) {
            Member findMember = cacheRepository.findMemberAtCache(loginId);
            List<TemporaryOrder> findTOrder = temporaryOrderService.findTOrderListByMemberId(findMember.getId());

            for (TemporaryOrder temporaryOrder : findTOrder) {
                totalPrice += temporaryOrder.getPrice() * temporaryOrder.getCount();
            }

            Coupon coupon = couponRepository.findByDetail(dto.getTarget()).orElse(null);

            percentage = 1 - (coupon.getPercentage() * 0.01);

            map.put("orderPrice", totalPrice * percentage);
            map.put("discountPrice", totalPrice - (totalPrice * percentage));
        }

        return map;
    }
}
