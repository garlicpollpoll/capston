package com.hello.capston.unit.repository.memberwhogetcoupon;

import com.hello.capston.entity.*;
import com.hello.capston.repository.*;
import com.hello.capston.unit.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class MemberWhoGetCouponRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    MemberWhoGetCouponRepository memberWhoGetCouponRepository;

    @Test
    @DisplayName("User Id 로 내 쿠폰 목록 조회하기")
    public void test() throws Exception {
        //given
        User user = Data.createUser();
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Coupon coupon = Data.createCoupon();
        MemberWhoGetCoupon memberWhoGetCoupon = Data.createMemberWhoGetCoupon(user, null, coupon);
        //when
        memberRepository.save(member);
        User saveUser = userRepository.save(user);
        itemRepository.save(item);
        couponRepository.save(coupon);
        memberWhoGetCouponRepository.save(memberWhoGetCoupon);

        List<MemberWhoGetCoupon> findCoupon = memberWhoGetCouponRepository.findCouponByUserId(saveUser.getId());
        //then
        Assertions.assertNotNull(findCoupon);
        Assertions.assertEquals(1, findCoupon.size());
        Assertions.assertEquals(coupon.getCouponDetail(), findCoupon.get(0).getCoupon().getCouponDetail());
    }

    @Test
    @DisplayName("Member Id 로 내 쿠폰 목록 조회하기")
    public void test2() throws Exception {
        //given
        User user = Data.createUser();
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Coupon coupon = Data.createCoupon();
        MemberWhoGetCoupon memberWhoGetCoupon = Data.createMemberWhoGetCoupon(null, member, coupon);
        //when
        Member saveMember = memberRepository.save(member);
        userRepository.save(user);
        itemRepository.save(item);
        couponRepository.save(coupon);
        memberWhoGetCouponRepository.save(memberWhoGetCoupon);

        List<MemberWhoGetCoupon> findCoupon = memberWhoGetCouponRepository.findCouponByMemberId(saveMember.getId());
        //then
        Assertions.assertNotNull(findCoupon);
        Assertions.assertEquals(1, findCoupon.size());
        Assertions.assertEquals(coupon.getCouponDetail(), findCoupon.get(0).getCoupon().getCouponDetail());
    }

    @Test
    @DisplayName("User Id 로 내가 아직 사용하지 않은 쿠폰 조회")
    public void test3() throws Exception {
        //given
        User user = Data.createUser();
        Member member = Data.createMember();
        Coupon coupon = Data.createCoupon();
        MemberWhoGetCoupon memberWhoGetCoupon = Data.createMemberWhoGetCoupon(user, null, coupon);
        //when
        memberRepository.save(member);
        User saveUser = userRepository.save(user);
        couponRepository.save(coupon);
        memberWhoGetCouponRepository.save(memberWhoGetCoupon);

        List<MemberWhoGetCoupon> findCoupon = memberWhoGetCouponRepository.findCouponByUserIdAndCheckUsed(saveUser.getId(), 0);
        //then
        Assertions.assertNotNull(findCoupon);
        Assertions.assertEquals(1, findCoupon.size());
        Assertions.assertEquals(coupon.getCouponDetail(), findCoupon.get(0).getCoupon().getCouponDetail());
    }

    @Test
    @DisplayName("Member Id 로 내가 아직 사용하지 않은 쿠폰 조회")
    public void test4() throws Exception {
        //given
        User user = Data.createUser();
        Member member = Data.createMember();
        Coupon coupon = Data.createCoupon();
        MemberWhoGetCoupon memberWhoGetCoupon = Data.createMemberWhoGetCoupon(null, member, coupon);
        //when
        Member saveMember = memberRepository.save(member);
        userRepository.save(user);
        couponRepository.save(coupon);
        memberWhoGetCouponRepository.save(memberWhoGetCoupon);

        List<MemberWhoGetCoupon> findCoupon = memberWhoGetCouponRepository.findCouponByMemberIdAndCheckUsed(saveMember.getId(), 0);
        //then
        Assertions.assertNotNull(findCoupon);
        Assertions.assertEquals(1, findCoupon.size());
        Assertions.assertEquals(coupon.getCouponDetail(), findCoupon.get(0).getCoupon().getCouponDetail());
    }

    @Test
    @DisplayName("쿠폰 설명과 User Id 를 이용해 쿠폰을 특정해 조회")
    public void test5() throws Exception {
        //given
        User user = Data.createUser();
        Coupon coupon = Data.createCoupon();
        MemberWhoGetCoupon memberWhoGetCoupon = Data.createMemberWhoGetCoupon(user, null, coupon);
        //when
        User saveUser = userRepository.save(user);
        Coupon saveCoupon = couponRepository.save(coupon);
        memberWhoGetCouponRepository.save(memberWhoGetCoupon);

        MemberWhoGetCoupon findCoupon = memberWhoGetCouponRepository.findByDetailAndUserId(saveCoupon.getCouponDetail(), saveUser.getId()).orElse(null);
        //then
        Assertions.assertNotNull(findCoupon);
        Assertions.assertEquals(coupon.getCouponDetail(), findCoupon.getCoupon().getCouponDetail());
    }

    @Test
    @DisplayName("쿠폰 설명과 Member Id 를 이용해 쿠폰을 특정해 조회")
    public void test6() throws Exception {
        //given
        User user = Data.createUser();
        Member member = Data.createMember();
        Coupon coupon = Data.createCoupon();
        MemberWhoGetCoupon memberWhoGetCoupon = Data.createMemberWhoGetCoupon(null, member, coupon);
        //when
        Member saveMember = memberRepository.save(member);
        User saveUser = userRepository.save(user);
        Coupon saveCoupon = couponRepository.save(coupon);
        memberWhoGetCouponRepository.save(memberWhoGetCoupon);

        MemberWhoGetCoupon findCoupon = memberWhoGetCouponRepository.findByDetailAndMemberId(saveCoupon.getCouponDetail(), saveMember.getId()).orElse(null);
        //then
        Assertions.assertNotNull(findCoupon);
        Assertions.assertEquals(coupon.getCouponDetail(), findCoupon.getCoupon().getCouponDetail());
    }
}
