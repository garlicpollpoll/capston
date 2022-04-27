package com.hello.capston.service;

import com.hello.capston.entity.Bucket;
import com.hello.capston.entity.Item;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import com.hello.capston.repository.BucketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BucketService {

    private final BucketRepository bucketRepository;

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

}
