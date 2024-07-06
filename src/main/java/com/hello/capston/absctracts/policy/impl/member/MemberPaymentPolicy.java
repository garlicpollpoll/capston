package com.hello.capston.absctracts.policy.impl.member;

import com.hello.capston.absctracts.policy.PaymentPolicy;
import com.hello.capston.absctracts.policy.impl.utils.CheckStockUtils;
import com.hello.capston.dto.dto.payment.LookUpPaymentCompleteDto;
import com.hello.capston.dto.dto.payment.LookUpPaymentDto;
import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.*;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.repository.cache.KeyGenerator;
import io.lettuce.core.support.caching.CacheFrontend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MemberPaymentPolicy implements PaymentPolicy {

    private final CacheRepository cacheRepository;
    private final OrderItemRepository orderItemRepository;

    private final BucketRepository bucketRepository;
    private final TemporaryOrderRepository temporaryOrderRepository;
    private final ItemDetailRepository itemDetailRepository;
    private final MemberWhoGetCouponRepository memberWhoGetCouponRepository;

    private final CheckStockUtils checkStockUtils;
    private final CacheFrontend<String, Member> frontend;

    @Override
    public LookUpPaymentCompleteDto paymentComplete(String username) {
        int orderPrice = 0;
        String key = KeyGenerator.memberKeyGenerate(username);
        Member findMember = frontend.get(key);

//        Member findMember = cacheRepository.findMemberAtCache(username);
        List<OrderItem> findOrderItem = orderItemRepository.findOrdersByMemberId(findMember.getId());
        for (OrderItem orderItem : findOrderItem) {
            orderPrice += orderItem.getCount() * orderItem.getOrderPrice();
        }
        MemberRole role = findMember.getRole();

        return new LookUpPaymentCompleteDto(findOrderItem, role, orderPrice);
    }

    @Override
    public LookUpPaymentDto lookUpPayment(String username) {
        String key = KeyGenerator.memberKeyGenerate(username);
        Member findMember = frontend.get(key);
//        Member findMember = cacheRepository.findMemberAtCache(username);
        List<Bucket> findBucket = bucketRepository.findByMemberId(findMember.getId());
        boolean checkStock = true;
        String message = null;
        String itemName = null;
        int orderPrice = 0;

        for (Bucket bucket : findBucket) {
            TemporaryOrder findTemporaryOrder = temporaryOrderRepository.findTemporaryOrderByBucketId(bucket.getId());


            List<ItemDetail> findItemDetail = itemDetailRepository.findByItemId(bucket.getItem().getId());

            Map<String, Object> map = checkStockUtils.checkStockAndRedirect(findTemporaryOrder, findItemDetail);
            checkStock = (boolean) map.get("checkStock");
            message = (String) map.get("message");

            orderPrice += findTemporaryOrder.getCount() * findTemporaryOrder.getPrice();
            itemName = bucket.getItem().getItemName();
        }

        MemberRole role = findMember.getRole();

        List<TemporaryOrder> findTOrder = temporaryOrderRepository.findTemporaryOrderByMemberId(findMember.getId());

        List<MemberWhoGetCoupon> findCoupon = memberWhoGetCouponRepository.findCouponByMemberIdAndCheckUsed(findMember.getId(), 0);

        return new LookUpPaymentDto(findCoupon, findTOrder, orderPrice, findTOrder.size(), itemName, role, checkStock, findMember, null, message);
    }
}
