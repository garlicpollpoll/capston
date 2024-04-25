package com.hello.capston.absctracts.policy.impl;

import com.hello.capston.absctracts.policy.Policy;
import com.hello.capston.dto.dto.CouponDto;
import com.hello.capston.dto.dto.coupon.CouponSettingDto;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.MemberWhoGetCoupon;
import com.hello.capston.repository.MemberWhoGetCouponRepository;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MemberPolicy implements Policy {

    private final CacheRepository cacheRepository;
    private final MemberWhoGetCouponRepository memberWhoGetCouponRepository;

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
    public Map<String, Double> selectCoupon() {
        return null;
    }
}
