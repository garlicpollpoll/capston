package com.hello.capston.service;

import com.hello.capston.entity.Bucket;
import com.hello.capston.entity.TemporaryOrder;
import com.hello.capston.repository.TemporaryOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemporaryOrderService {

    private final TemporaryOrderRepository temporaryOrderRepository;

    public TemporaryOrder findByBucketId(Long bucketId) {
        return temporaryOrderRepository.findTemporaryOrderByBucketId(bucketId);
    }

    public List<TemporaryOrder> findTOrderListByMemberId(Long memberId) {
        return temporaryOrderRepository.findTemporaryOrderByMemberId(memberId);
    }

    public List<TemporaryOrder> findTOrderListByUserId(Long userId) {
        return temporaryOrderRepository.findTemporaryOrderByUserId(userId);
    }

    public TemporaryOrder save(Bucket bucket, int price, String size) {
        TemporaryOrder tOrder = new TemporaryOrder(bucket, 1, price, size);
        return temporaryOrderRepository.save(tOrder);
    }

    public void delete(TemporaryOrder temporaryOrder) {
        temporaryOrderRepository.delete(temporaryOrder);
    }
}
