package com.hello.capston.absctracts.policy.impl.social;

import com.hello.capston.absctracts.policy.CouponPolicy;
import com.hello.capston.dto.dto.CouponDto;
import com.hello.capston.dto.dto.SelectCouponDto;
import com.hello.capston.dto.dto.coupon.CouponSettingDto;
import com.hello.capston.entity.MemberWhoGetCoupon;
import com.hello.capston.entity.User;
import com.hello.capston.repository.MemberWhoGetCouponRepository;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserCouponPolicy implements CouponPolicy {

    private final CacheRepository cacheRepository;
    private final MemberWhoGetCouponRepository memberWhoGetCouponRepository;

    @Override
    public CouponDto isCoupon(CouponSettingDto dto) {
        Map<String, String> map = new HashMap<>();
        boolean isCouponHas = false;
        User findUser = cacheRepository.findUserAtCache(dto.getUsername());
        List<MemberWhoGetCoupon> findCoupon = memberWhoGetCouponRepository.findCouponByUserId(findUser.getId());

        if (findCoupon.isEmpty()) {
            memberWhoGetCouponRepository.save(new MemberWhoGetCoupon(findUser, null, dto.getCoupon(), 0));
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
                memberWhoGetCouponRepository.save(new MemberWhoGetCoupon(findUser, null, dto.getCoupon(), 0));
                map.put("message", "쿠폰이 등록되었습니다.");
                map.put("url", "/");
                return new CouponDto(map, isCouponHas);
            }
        }
        return null;
    }

    @Override
    public Map<String, Double> selectCoupon(String username, SelectCouponDto dto) {
        return null;
    }
}
