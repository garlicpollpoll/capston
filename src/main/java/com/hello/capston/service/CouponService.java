package com.hello.capston.service;

import com.hello.capston.absctracts.policy.Policy;
import com.hello.capston.absctracts.policy.config.PolicyManager;
import com.hello.capston.dto.dto.CouponDto;
import com.hello.capston.dto.dto.SelectCouponDto;
import com.hello.capston.dto.dto.coupon.CouponSettingDto;
import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.CouponRepository;
import com.hello.capston.repository.MemberWhoGetCouponRepository;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final WhatIsRoleService roleService;

    private final PolicyManager policyManager;


    @Transactional
    public CouponDto isCoupon(Authentication authentication, String code) {
        Map<String, String> map = new HashMap<>();
        Coupon coupon = couponRepository.findByCode(code).orElse(null);
        boolean isCouponHas = false;
        CouponSettingDto couponSettingDto = new CouponSettingDto();

        MemberRole memberRole = roleService.whatIsRole(authentication);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        couponSettingDto.setUsername(username);
        couponSettingDto.setCoupon(coupon);
        couponSettingDto.setCode(code);

        Policy policy = policyManager.policy(memberRole);

        if (coupon == null) {
            map.put("message", "존재하지 않는 쿠폰입니다.");
            map.put("url", "/coupon");
            return new CouponDto(map, isCouponHas);
        }

        CouponDto couponDto = policy.isCoupon(couponSettingDto);

        return couponDto;
    }

    public Map<String, Double> selectCoupon(Authentication authentication, SelectCouponDto dto) {
        Map<String, Double> map = new HashMap<>();
        int totalPrice = 0;
        double percentage = 0;

        MemberRole memberRole = roleService.whatIsRole(authentication);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        Policy policy = policyManager.policy(memberRole);

        if (isRoleMember(memberRole)) {
            Member findMember = cacheRepository.findMemberAtCache(username);
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

    private static boolean isRoleMember(MemberRole memberRole) {
        return memberRole.equals(MemberRole.ROLE_MEMBER) || memberRole.equals(MemberRole.ROLE_SOCIAL);
    }
}
