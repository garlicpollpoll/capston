package com.hello.capston.absctracts.policy.config;

import com.hello.capston.absctracts.policy.CouponPolicy;
import com.hello.capston.absctracts.policy.PaymentPolicy;
import com.hello.capston.absctracts.policy.impl.member.MemberCouponPolicy;
import com.hello.capston.absctracts.policy.impl.member.MemberPaymentPolicy;
import com.hello.capston.absctracts.policy.impl.social.UserCouponPolicy;
import com.hello.capston.absctracts.policy.impl.social.UserPaymentPolicy;
import com.hello.capston.absctracts.policy.impl.utils.CheckStockUtils;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.*;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.CouponService;
import com.hello.capston.service.TemporaryOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PolicyManager {

    // dependency for all
    private final CacheRepository cacheRepository;

    // dependency for coupon
    private final MemberWhoGetCouponRepository memberWhoGetCouponRepository;
    private final CouponRepository couponRepository;
    private final TemporaryOrderService temporaryOrderService;
    private final CouponService couponService;

    // dependency for payment
    private final OrderItemRepository orderItemRepository;
    private final BucketRepository bucketRepository;
    private final TemporaryOrderRepository temporaryOrderRepository;
    private final ItemDetailRepository itemDetailRepository;

    private final CheckStockUtils checkStockUtils;

    public CouponPolicy couponPolicy(MemberRole role) {
        if (role.equals(MemberRole.ROLE_SOCIAL)) {
            return new UserCouponPolicy(cacheRepository, memberWhoGetCouponRepository);
        }
        else {
            return new MemberCouponPolicy(cacheRepository, memberWhoGetCouponRepository, couponRepository, temporaryOrderService, couponService);
        }
    }

    public PaymentPolicy paymentPolicy(MemberRole role) {
        if (role.equals(MemberRole.ROLE_SOCIAL)) {
            return new UserPaymentPolicy(cacheRepository, orderItemRepository, bucketRepository, temporaryOrderRepository, itemDetailRepository, memberWhoGetCouponRepository, checkStockUtils);
        }
        else {
            return new MemberPaymentPolicy(cacheRepository, orderItemRepository, bucketRepository, temporaryOrderRepository, itemDetailRepository, memberWhoGetCouponRepository, checkStockUtils);
        }
    }
}
