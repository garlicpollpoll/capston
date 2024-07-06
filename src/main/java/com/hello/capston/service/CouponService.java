package com.hello.capston.service;

import com.hello.capston.absctracts.policy.CouponPolicy;
import com.hello.capston.absctracts.policy.config.PolicyManager;
import com.hello.capston.dto.dto.coupon.CouponDto;
import com.hello.capston.dto.request.SelectCouponDto;
import com.hello.capston.dto.dto.coupon.CouponSettingDto;
import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.CouponRepository;
import com.hello.capston.repository.MemberWhoGetCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final WhatIsRoleService roleService;

    private final PolicyManager policyManager;
    private final MemberWhoGetCouponRepository memberWhoGetCouponRepository;


    @Transactional
    public CouponDto isCoupon(Authentication authentication, String code) {
        Map<String, String> map = new HashMap<>();
        Coupon coupon = couponRepository.findByCode(code).orElse(null);
        boolean isCouponHas = false;
        CouponSettingDto couponSettingDto = new CouponSettingDto();

        MemberRole memberRole = roleService.whatIsRole(authentication);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        if (coupon == null) {
            map.put("message", "존재하지 않는 쿠폰입니다.");
            map.put("url", "/coupon");
            return new CouponDto(map, isCouponHas);
        }

        couponSettingDto.setUsername(username);
        couponSettingDto.setCoupon(coupon);
        couponSettingDto.setCode(code);

        CouponPolicy policy = policyManager.couponPolicy(memberRole);
        CouponDto couponDto = policy.isCoupon(couponSettingDto);

        return couponDto;
    }

    public Map<String, Double> selectCoupon(Authentication authentication, SelectCouponDto dto) {

        MemberRole memberRole = roleService.whatIsRole(authentication);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        CouponPolicy policy = policyManager.couponPolicy(memberRole);
        Map<String, Double> map = policy.selectCoupon(username, dto);

        return map;
    }

    public List<MemberWhoGetCoupon> getMemberWheGetCouponList(Long memberId) {
        List<MemberWhoGetCoupon> findCoupon = memberWhoGetCouponRepository.findCouponByMemberId(memberId);

        if (findCoupon == null) {
            return Collections.emptyList();
        }
        else {
            return findCoupon;
        }
    }
}
