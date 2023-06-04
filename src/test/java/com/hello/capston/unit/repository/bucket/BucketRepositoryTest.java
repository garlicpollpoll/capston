package com.hello.capston.unit.repository.bucket;

import com.hello.capston.entity.Bucket;
import com.hello.capston.entity.Item;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import com.hello.capston.repository.BucketRepository;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.UserRepository;
import com.hello.capston.unit.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class BucketRepositoryTest {

    @Autowired
    BucketRepository bucketRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Member Id 로 총 Bucket 찾기")
    public void test1() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Bucket bucket = Data.createBucket(member, null, item);
        //when
        Member saveMember = memberRepository.save(member);
        itemRepository.save(item);
        bucketRepository.save(bucket);

        List<Bucket> findBucket = bucketRepository.findByMemberId(saveMember.getId());
        //then
        Assertions.assertNotNull(findBucket);
        Assertions.assertEquals(1, findBucket.size());
        Assertions.assertEquals(item.getItemName(), findBucket.get(0).getItem().getItemName());
    }

    @Test
    @DisplayName("User Id 로 총 Bucket 찾기")
    public void test2() throws Exception {
        //given
        User user = Data.createUser();
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Bucket bucket = Data.createBucket(null, user, item);
        //when
        memberRepository.save(member);
        User saveUser = userRepository.save(user);
        itemRepository.save(item);
        bucketRepository.save(bucket);

        List<Bucket> findBucket = bucketRepository.findByUserId(saveUser.getId());
        //then
        Assertions.assertNotNull(findBucket);
        Assertions.assertEquals(1, findBucket.size());
        Assertions.assertEquals(item.getItemName(), findBucket.get(0).getItem().getItemName());
    }

    @Test
    @DisplayName("Member Id 를 이용해서 장바구니의 총액을 구하는 쿼리")
    public void test3() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Bucket bucket = Data.createBucket(member, null, item);
        //when
        Member saveMember = memberRepository.save(member);
        itemRepository.save(item);
        bucketRepository.save(bucket);

        Integer totalAmount = bucketRepository.findTotalAmountByMemberId(saveMember.getId()).orElse(null);
        //then
        Assertions.assertNotNull(totalAmount);
        Assertions.assertEquals(1000, totalAmount);
    }

    @Test
    @DisplayName("User Id 로 장바구니의 총액을 구하는 쿼리")
    public void test4() throws Exception {
        //given
        Member member = Data.createMember();
        User user = Data.createUser();
        Item item = Data.createItem(member);
        Bucket bucket = Data.createBucket(null, user, item);
        //when
        memberRepository.save(member);
        User saveUser = userRepository.save(user);
        itemRepository.save(item);
        bucketRepository.save(bucket);

        Integer totalAmount = bucketRepository.findTotalAmountByUserId(saveUser.getId()).orElse(null);
        //then
        Assertions.assertNotNull(totalAmount);
        Assertions.assertEquals(1000, totalAmount);
    }
}
