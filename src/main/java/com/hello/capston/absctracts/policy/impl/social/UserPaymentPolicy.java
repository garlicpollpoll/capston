package com.hello.capston.absctracts.policy.impl.social;

import com.hello.capston.absctracts.policy.PaymentPolicy;
import com.hello.capston.absctracts.policy.impl.utils.CheckStockUtils;
import com.hello.capston.dto.dto.payment.LookUpPaymentCompleteDto;
import com.hello.capston.dto.dto.payment.LookUpPaymentDto;
import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.*;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserPaymentPolicy implements PaymentPolicy {

    private final CacheRepository cacheRepository;
    private final OrderItemRepository orderItemRepository;

    private final BucketRepository bucketRepository;
    private final TemporaryOrderRepository temporaryOrderRepository;
    private final ItemDetailRepository itemDetailRepository;
    private final MemberWhoGetCouponRepository memberWhoGetCouponRepository;

    private final CheckStockUtils checkStockAndRedirect;

    @Override
    public LookUpPaymentCompleteDto paymentComplete(String username) {
        int orderPrice = 0;
        Member findMember = cacheRepository.findMemberAtCache(username);
        List<OrderItem> findOrderItem = orderItemRepository.findOrdersByMemberId(findMember.getId());
        for (OrderItem orderItem : findOrderItem) {
            orderPrice += orderItem.getCount() * orderItem.getOrderPrice();
        }
        MemberRole role = findMember.getRole();

        return new LookUpPaymentCompleteDto(findOrderItem, role, orderPrice);
    }

    @Override
    public LookUpPaymentDto lookUpPayment(String username) {
        User findUser = cacheRepository.findUserAtCache(username);
        List<Bucket> findBucket = bucketRepository.findByUserId(findUser.getId());
        boolean checkStock = true;
        String message = null;
        String itemName = null;
        int orderPrice = 0;

        for (Bucket bucket : findBucket) {
            TemporaryOrder findTemporaryOrder = temporaryOrderRepository.findTemporaryOrderByBucketId(bucket.getId());

            List<ItemDetail> findItemDetail = itemDetailRepository.findByItemId(bucket.getItem().getId());

            Map<String, Object> map = checkStockAndRedirect.checkStockAndRedirect(findTemporaryOrder, findItemDetail);
            checkStock = (boolean) map.get("checkStock");
            message = (String) map.get("message");

            orderPrice += findTemporaryOrder.getCount() * findTemporaryOrder.getPrice();
            itemName = bucket.getItem().getItemName();
        }

        List<TemporaryOrder> findTOrder = temporaryOrderRepository.findTemporaryOrderByUserId(findUser.getId());

        List<MemberWhoGetCoupon> findCoupon = memberWhoGetCouponRepository.findCouponByUserIdAndCheckUsed(findUser.getId(), 0);

        return new LookUpPaymentDto(findCoupon, findTOrder, orderPrice, findTOrder.size(), itemName, null, checkStock, null, findUser, message);
    }
}
