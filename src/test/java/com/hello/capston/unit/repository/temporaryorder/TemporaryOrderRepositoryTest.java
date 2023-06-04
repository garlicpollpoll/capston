package com.hello.capston.unit.repository.temporaryorder;

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
public class TemporaryOrderRepositoryTest {

    @Autowired
    TemporaryOrderRepository temporaryOrderRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    BucketRepository bucketRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Bucket (장바구니 한 행 row) Id 를 이용해 임시 주문 정보 조회하기")
    public void test() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Bucket bucket = Data.createBucket(member, null, item);
        TemporaryOrder tOrder = Data.createTOrder(bucket);
        //when
        memberRepository.save(member);
        itemRepository.save(item);
        Bucket saveBucket = bucketRepository.save(bucket);
        temporaryOrderRepository.save(tOrder);

        TemporaryOrder findTOrder = temporaryOrderRepository.findTemporaryOrderByBucketId(saveBucket.getId());
        //then
        Assertions.assertNotNull(findTOrder);
        Assertions.assertEquals(bucket.getItem().getItemName(), findTOrder.getBucket().getItem().getItemName());
    }

    @Test
    @DisplayName("Member Id 를 이용해 내 장바구니 조회하기")
    public void test2() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Bucket bucket = Data.createBucket(member, null, item);
        TemporaryOrder tOrder = Data.createTOrder(bucket);
        //when
        Member saveMember = memberRepository.save(member);
        itemRepository.save(item);
        bucketRepository.save(bucket);
        temporaryOrderRepository.save(tOrder);

        List<TemporaryOrder> findTOrder = temporaryOrderRepository.findTemporaryOrderByMemberId(saveMember.getId());
        //then
        Assertions.assertNotNull(findTOrder);
        Assertions.assertEquals(1, findTOrder.size());
        Assertions.assertEquals(bucket.getItem().getItemName(), findTOrder.get(0).getBucket().getItem().getItemName());
    }

    @Test
    @DisplayName("User Id 를 이용해 내 장바구니 조회하기")
    public void test3() throws Exception {
        //given
        Member member = Data.createMember();
        User user = Data.createUser();
        Item item = Data.createItem(member);
        Bucket bucket = Data.createBucket(null, user, item);
        TemporaryOrder tOrder = Data.createTOrder(bucket);
        //when
        memberRepository.save(member);
        User saveUser = userRepository.save(user);
        itemRepository.save(item);
        bucketRepository.save(bucket);
        temporaryOrderRepository.save(tOrder);

        List<TemporaryOrder> findTOrder = temporaryOrderRepository.findTemporaryOrderByUserId(saveUser.getId());
        //then
        Assertions.assertNotNull(findTOrder);
        Assertions.assertEquals(1, findTOrder.size());
        Assertions.assertEquals(bucket.getItem().getItemName(), findTOrder.get(0).getBucket().getItem().getItemName());
    }
}
