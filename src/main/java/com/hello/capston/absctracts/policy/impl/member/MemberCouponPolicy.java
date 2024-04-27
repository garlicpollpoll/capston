package com.hello.capston.absctracts.policy.impl.member;

import com.hello.capston.absctracts.policy.CouponPolicy;
import com.hello.capston.dto.dto.CouponDto;
import com.hello.capston.dto.dto.SelectCouponDto;
import com.hello.capston.dto.dto.coupon.CouponSettingDto;
import com.hello.capston.entity.*;
import com.hello.capston.repository.CouponRepository;
import com.hello.capston.repository.MemberWhoGetCouponRepository;
import com.hello.capston.repository.OrderItemRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.TemporaryOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MemberCouponPolicy implements CouponPolicy {

    private final CacheRepository cacheRepository;
    private final MemberWhoGetCouponRepository memberWhoGetCouponRepository;
    private final CouponRepository couponRepository;
    private final TemporaryOrderService temporaryOrderService;

    @Override
    public CouponDto isCoupon(CouponSettingDto dto) {
        Map<String, String> map = new HashMap<>();
        boolean isCouponHas = false;
        Member findMember = cacheRepository.findMemberAtCache(dto.getUsername());
        List<MemberWhoGetCoupon> findCoupon = memberWhoGetCouponRepository.findCouponByMemberId(findMember.getId());

        if (findCoupon.isEmpty()) {
            memberWhoGetCouponRepository.save(new MemberWhoGetCoupon(null, findMember, dto.getCoupon(), 0));
            map.put("message", "쿠폰이 등록되었습니다.");
            map.put("url", "/");
            return new CouponDto(map, isCouponHas);
        }

        for (MemberWhoGetCoupon memberWhoGetCoupon : findCoupon) {
            if (memberWhoGetCoupon.getCoupon().getCode().equals(dto.getCode())) {
                map.put("message", "이미 가지고 있는 쿠폰입니다.");
                map.put("url", "/coupon");
                isCouponHas = true;
                return new CouponDto(map, isCouponHas);
            }
            else {
                memberWhoGetCouponRepository.save(new MemberWhoGetCoupon(null, findMember, dto.getCoupon(), 0));
                map.put("message", "쿠폰이 등록되었습니다.");
                map.put("url", "/");
                return new CouponDto(map, isCouponHas);
            }
        }
        return null;
    }

    @Override
    public Map<String, Double> selectCoupon(String username, SelectCouponDto dto) {
        Map<String, Double> map = new HashMap<>();
        int totalPrice = 0;
        double percentage = 0;

        Member findMember = cacheRepository.findMemberAtCache(username);
        List<TemporaryOrder> findTOrder = temporaryOrderService.findTOrderListByMemberId(findMember.getId());

        for (TemporaryOrder temporaryOrder : findTOrder) {
            totalPrice += temporaryOrder.getPrice() * temporaryOrder.getCount();
        }

        Coupon coupon = couponRepository.findByDetail(dto.getTarget()).orElse(null);

        percentage = 1 - (coupon.getPercentage() * 0.01);

        map.put("orderPrice", totalPrice * percentage);
        map.put("discountPrice", totalPrice - (totalPrice * percentage));

        return map;
    }
}
