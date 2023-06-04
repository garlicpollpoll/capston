package com.hello.capston.service;

import com.hello.capston.dto.dto.ChangeCountDto;
import com.hello.capston.dto.dto.bucket.LookUpBucketDto;
import com.hello.capston.dto.form.BucketForm;
import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.BucketRepository;
import com.hello.capston.repository.ItemDetailRepository;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.repository.TemporaryOrderRepository;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BucketService {

    private final BucketRepository bucketRepository;
    private final TemporaryOrderRepository temporaryOrderRepository;
    private final ItemDetailRepository itemDetailRepository;
    private final CacheRepository cacheRepository;
    private final ItemRepository itemRepository;
    private final TemporaryOrderService temporaryOrderService;

    public List<Bucket> findBucketByMemberId(Long memberId) {
        return bucketRepository.findByMemberId(memberId);
    }

    public List<Bucket> findBucketByUserId(Long userId) {
        return bucketRepository.findByUserId(userId);
    }

    public Bucket findById(Long bucketId) {
        return bucketRepository.findById(bucketId).orElse(null);
    }

    public void delete(Bucket bucket) {
        bucketRepository.delete(bucket);
    }

    public int findTotalAmountByMemberId(Long memberId) {
        return bucketRepository.findTotalAmountByMemberId(memberId).orElse(0);
    }

    public int findTotalAmountByUserId(Long userId) {
        return bucketRepository.findTotalAmountByUserId(userId).orElse(0);
    }

    public Bucket save(Member member, User user, Item item, int orders) {
        Bucket bucket = new Bucket(member, item, user, orders + 1);
        bucketRepository.save(bucket);

        return bucket;
    }

    public Map<String, String> isBucketStockZero(ChangeCountDto dto) {
        Map<String, String> map = new HashMap<>();
        int nowStock = 0;
        Bucket findBucket = bucketRepository.findById(Long.parseLong(dto.getBucketId())).orElseThrow(
                () -> new RuntimeException("Not Found Bucket")
        );

        TemporaryOrder tOrder = temporaryOrderRepository.findTemporaryOrderByBucketId(findBucket.getId());

        List<ItemDetail> findItemDetails = itemDetailRepository.findByItemId(findBucket.getItem().getId());

        for (ItemDetail itemDetail : findItemDetails) {
            nowStock = itemDetail.getStock() - Integer.parseInt(dto.getCount());

            if (nowStock <= -1) {
                map.put("message", "재고가 남아있지 않습니다. 남은 수량 : " + itemDetail.getStock());
            }
        }
        TemporaryOrder temporaryOrder = tOrder.changeCount(Integer.parseInt(dto.getCount()));
        map.put("count", String.valueOf(temporaryOrder.getCount()));

        return map;
    }

    public Bucket addBucket(String loginId, String userEmail, BucketForm form) {
        Integer orders = 0;
        User findUser = null;
        Member findMember = null;

        Item findItem = itemRepository.findById(Long.parseLong(form.getId())).orElse(new Item());

        if (loginId == null) {
            findUser = cacheRepository.findUserAtCache(userEmail);
            List<Bucket> findBucket = bucketRepository.findByUserId(findUser.getId());
            orders = findBucket.size();
        }

        if (userEmail == null) {
            findMember = cacheRepository.findMemberAtCache(loginId);
            List<Bucket> findBucket = bucketRepository.findByMemberId(findMember.getId());
            orders = findBucket.size();
        }

        if (orders == null) {
            orders = 1;
        }

        Bucket bucket = save(findMember, findUser, findItem, orders);

        temporaryOrderService.save(bucket, findItem.getPrice(), form.getSize());

        return bucket;
    }

    // TODO bucket, bucketCount, totalAmount, status
    public LookUpBucketDto lookUpMyBucket(String loginId, String userEmail) {
        Map<String, Object> map = new HashMap<>();
        User findUser = null;
        Member findMember = null;
        List<TemporaryOrder> myBucket = new ArrayList<>();
        Integer totalAmount = null;
        MemberRole role = null;

        if (loginId == null) {
            findUser = cacheRepository.findUserAtCache(userEmail);
            myBucket = temporaryOrderService.findTOrderListByUserId(findUser.getId());

            totalAmount = findTotalAmountByUserId(findUser.getId());
        }

        if (userEmail == null) {
            findMember = cacheRepository.findMemberAtCache(loginId);
            myBucket = temporaryOrderService.findTOrderListByMemberId(findMember.getId());

            totalAmount = findTotalAmountByMemberId(findMember.getId());
            role = findMember.getRole();
        }

        return new LookUpBucketDto(myBucket, myBucket.size(), totalAmount, role);
    }

    public void cancelBucket(BucketForm form) {
        Bucket findBucket = findById(Long.parseLong(form.getId()));

        TemporaryOrder tOrder = temporaryOrderRepository.findTemporaryOrderByBucketId(Long.parseLong(form.getId()));

        temporaryOrderRepository.delete(tOrder);
        delete(findBucket);
    }
}
