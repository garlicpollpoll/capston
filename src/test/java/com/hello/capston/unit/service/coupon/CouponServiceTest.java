package com.hello.capston.unit.service.coupon;

import com.hello.capston.dto.dto.CouponDto;
import com.hello.capston.dto.dto.SelectCouponDto;
import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.entity.enums.Role;
import com.hello.capston.repository.CouponRepository;
import com.hello.capston.repository.MemberWhoGetCouponRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.CouponService;
import com.hello.capston.service.TemporaryOrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    CacheRepository cacheRepository;

    @Mock
    MemberWhoGetCouponRepository memberWhoGetCouponRepository;

    @Mock
    CouponRepository couponRepository;

    @Mock
    TemporaryOrderService temporaryOrderService;

    @InjectMocks
    CouponService couponService;

    @Test
    @DisplayName("Member == null, User == kyoungsuk3254@naver.com")
    public void test1() throws Exception {
        //given
        String loginId = null;
        String userEmail = "kyoungsuk3254@naver.com";
        String code = "code";

        User user = new User(1L);
        Coupon coupon = new Coupon("image", "detail", 10, "date", code);

        when(cacheRepository.findUserAtCache(userEmail)).thenReturn(user);
        when(couponRepository.findByCode(code)).thenReturn(Optional.of(coupon));
        when(memberWhoGetCouponRepository.findCouponByUserId(user.getId())).thenReturn(Collections.emptyList());
        //when
        CouponDto dto = couponService.isCoupon(loginId, userEmail, code);
        //then
        Assertions.assertNotNull(dto);
        Assertions.assertEquals("쿠폰이 등록되었습니다.", dto.getMap().get("message"));
        Assertions.assertEquals("/", dto.getMap().get("url"));
    }

    @Test
    @DisplayName("Member == ks3254, User == null")
    public void test2() throws Exception {
        //given
        String loginId = "ks3254";
        String userEmail = null;
        String code = "code";

        Coupon coupon = new Coupon("image", "detail", 10, "date", code);
        Member member = createMember();

        when(couponRepository.findByCode(code)).thenReturn(Optional.of(coupon));
        when(cacheRepository.findMemberAtCache(loginId)).thenReturn(member);
        when(memberWhoGetCouponRepository.findCouponByMemberId(member.getId())).thenReturn(Collections.emptyList());
        //when
        CouponDto dto = couponService.isCoupon(loginId, userEmail, code);
        //then
        Assertions.assertNotNull(dto);
        Assertions.assertEquals("쿠폰이 등록되었습니다.", dto.getMap().get("message"));
        Assertions.assertEquals("/", dto.getMap().get("url"));
    }

    @Test
    @DisplayName("Coupon 을 가지고 있는 상황에서 또 쿠폰 등록을 하려는 경우, User == null")
    public void test3() throws Exception {
        //given
        String loginId = "ks3254";
        String userEmail = null;
        String code = "code";

        Member member = createMember();
        Coupon coupon = createCoupon();
        MemberWhoGetCoupon memberWhoGetCoupon = createMemberWhoGetCoupon(null, member, coupon);

        when(cacheRepository.findMemberAtCache(loginId)).thenReturn(member);
        when(couponRepository.findByCode(code)).thenReturn(Optional.of(coupon));
        when(memberWhoGetCouponRepository.findCouponByMemberId(member.getId())).thenReturn(Collections.singletonList(memberWhoGetCoupon));
        //when
        CouponDto dto = couponService.isCoupon(loginId, userEmail, code);
        //then
        Assertions.assertNotNull(dto);
        Assertions.assertEquals("이미 가지고 있는 쿠폰입니다.", dto.getMap().get("message"));
        Assertions.assertEquals("/coupon", dto.getMap().get("url"));
    }

    @Test
    @DisplayName("Coupon 을 가지고 있는 상황에서 또 쿠폰 등록을 하려는 경우, Member == null")
    public void test4() throws Exception {
        //given
        String loginId = null;
        String userEmail = "kyoungsuk3254@naver.com";
        String code = "code";

        Coupon coupon = createCoupon();
        User user = createUser();
        MemberWhoGetCoupon memberWhoGetCoupon = createMemberWhoGetCoupon(user, null, coupon);

        when(couponRepository.findByCode(code)).thenReturn(Optional.of(coupon));
        when(cacheRepository.findUserAtCache(userEmail)).thenReturn(user);
        when(memberWhoGetCouponRepository.findCouponByUserId(user.getId())).thenReturn(Collections.singletonList(memberWhoGetCoupon));
        //when
        CouponDto dto = couponService.isCoupon(loginId, userEmail, code);
        //then
        Assertions.assertNotNull(dto);
        Assertions.assertEquals("이미 가지고 있는 쿠폰입니다.", dto.getMap().get("message"));
        Assertions.assertEquals("/coupon", dto.getMap().get("url"));
    }

    @Test
    @DisplayName("쿠폰을 선택했을 때 할인율이 적용되어야 한다, User == null")
    public void test5() throws Exception {
        //given
        String loginId = "ks3254";
        String userEmail = null;
        SelectCouponDto dto = new SelectCouponDto("detail");

        Member member = createMember();
        Item item = createItem(member);
        Bucket bucket = createBucket(member, null, item);
        TemporaryOrder tOrder = createTOrder(bucket);
        Coupon coupon = createCoupon();

        when(cacheRepository.findMemberAtCache(loginId)).thenReturn(member);
        when(temporaryOrderService.findTOrderListByMemberId(member.getId())).thenReturn(Collections.singletonList(tOrder));
        when(couponRepository.findByDetail(dto.getTarget())).thenReturn(Optional.of(coupon));
        //when
        Map<String, Double> map = couponService.selectCoupon(loginId, userEmail, dto);
        //then
        Assertions.assertNotNull(map);
        Assertions.assertEquals(900, map.get("orderPrice"));
        Assertions.assertEquals(100, map.get("discountPrice"));
    }

    @Test
    @DisplayName("쿠폰을 선택했을 때 할인율이 적용되어야 한다, Member == null")
    public void test6() throws Exception {
        //given
        String loginId = null;
        String userEmail = "kyoungsuk3254@naver.com";
        SelectCouponDto dto = new SelectCouponDto("detail");

        User user = createUser();
        Member member = createMember();
        Item item = createItem(member);
        Bucket bucket = createBucket(member, null, item);
        TemporaryOrder tOrder = createTOrder(bucket);
        Coupon coupon = createCoupon();

        when(cacheRepository.findUserAtCache(userEmail)).thenReturn(user);
        when(temporaryOrderService.findTOrderListByUserId(user.getId())).thenReturn(Collections.singletonList(tOrder));
        when(couponRepository.findByDetail(dto.getTarget())).thenReturn(Optional.of(coupon));
        //when
        Map<String, Double> map = couponService.selectCoupon(loginId, userEmail, dto);
        //then
        Assertions.assertNotNull(map);
        Assertions.assertEquals(900, map.get("orderPrice"));
        Assertions.assertEquals(100, map.get("discountPrice"));
    }

    private Item createItem(Member member) {
        return new Item("brandName", "viewName", "itemName", "url", 1000, "code", member, "category", "color", 0, 0);
    }

    private Bucket createBucket(Member member, User user, Item item) {
        return new Bucket(member, item, user, 1);
    }

    private TemporaryOrder createTOrder(Bucket bucket) {
        return new TemporaryOrder(bucket, 1, 1000, "XL");
    }

    private MemberWhoGetCoupon createMemberWhoGetCoupon(User user, Member member, Coupon coupon) {
        return new MemberWhoGetCoupon(user, member, coupon, 0);
    }

    private Member createMember() {
        return new Member("username", "password", "birth", "gender", MemberRole.ROLE_MEMBER, "email", "session");
    }

    private User createUser() {
        return new User("name", "email", "picture", Role.USER, "key", "session");
    }

    private Coupon createCoupon() {
        return new Coupon("image", "detail", 10, "date", "code");
    }
}
