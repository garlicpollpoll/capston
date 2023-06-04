package com.hello.capston.unit.service.bucket;

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
import com.hello.capston.service.BucketService;
import com.hello.capston.service.TemporaryOrderService;
import com.hello.capston.unit.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BucketServiceTest {

    @Mock
    BucketRepository bucketRepository;
    @Mock
    TemporaryOrderRepository temporaryOrderRepository;
    @Mock
    ItemDetailRepository itemDetailRepository;
    @Mock
    CacheRepository cacheRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    TemporaryOrderService temporaryOrderService;
    @InjectMocks
    BucketService bucketService;

    @Test
    @DisplayName("장바구니에서 수량을 줄이거나 늘리면 개수가 변해야한다.")
    public void test1() throws Exception {
        //given
        ChangeCountDto dto = new ChangeCountDto();
        dto.setBucketId("1");
        dto.setCount("2");

        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Bucket bucket = Data.createBucket(member, null, item);
        TemporaryOrder tOrder = Data.createTOrder(bucket);
        ItemDetail itemDetail = Data.createItemDetail(item);


        when(bucketRepository.findById(Long.parseLong(dto.getBucketId()))).thenReturn(Optional.of(bucket));
        when(temporaryOrderRepository.findTemporaryOrderByBucketId(bucket.getId())).thenReturn(tOrder);
        when(itemDetailRepository.findByItemId(bucket.getItem().getId())).thenReturn(Collections.singletonList(itemDetail));
        //when
        Map<String, String> map = bucketService.isBucketStockZero(dto);
        //then
        Assertions.assertNotNull(map);
        Assertions.assertEquals(map.get("message"), "재고가 남아있지 않습니다. 남은 수량 : 1");
        Assertions.assertEquals(map.get("count"), "2");
    }

    @Test
    @DisplayName("장바구니에 저장이 되어야한다. and Member == null")
    public void test2() throws Exception {
        //given
        String loginId = null;
        String userEmail = "kyoungsuk3254@naver.com";

        BucketForm form = new BucketForm();
        String itemId = "1";
        String size = "XL";
        form.setId(itemId);
        form.setSize(size);

        Member member = Data.createMember();
        User user = Data.createUser();
        Item item = Data.createItem(member);
        Bucket bucket = Data.createBucket(member, user, item);

        when(itemRepository.findById(Long.parseLong(form.getId()))).thenReturn(Optional.of(item));
        when(cacheRepository.findUserAtCache(userEmail)).thenReturn(user);
        when(bucketRepository.findByUserId(user.getId())).thenReturn(Collections.singletonList(bucket));

        when(bucketRepository.save(any(Bucket.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //when
        Bucket saveBucket = bucketService.addBucket(loginId, userEmail, form);
        //then
        Assertions.assertNotNull(saveBucket);
    }

    @Test
    @DisplayName("장바구니에 저장이 되어야한다. and User == null")
    public void test3() throws Exception {
        //given
        String loginId = "ks3254";
        String userEmail = null;

        BucketForm form = new BucketForm();
        String itemId = "1";
        String size = "XL";
        form.setId(itemId);
        form.setSize(size);

        Member member = Data.createMember();
        User user = Data.createUser();
        Item item = Data.createItem(member);
        Bucket bucket = Data.createBucket(member, user, item);

        when(itemRepository.findById(Long.parseLong(form.getId()))).thenReturn(Optional.of(item));
        when(cacheRepository.findMemberAtCache(loginId)).thenReturn(member);
        when(bucketRepository.findByMemberId(member.getId())).thenReturn(Collections.singletonList(bucket));

        when(bucketRepository.save(any(Bucket.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //when
        Bucket saveBucket = bucketService.addBucket(loginId, userEmail, form);
        //then
        Assertions.assertNotNull(saveBucket);
    }

    @Test
    @DisplayName("장바구니를 조회할 때 필요한 데이터를 가져오는 로직, and Member == null")
    public void test4() throws Exception {
        //given
        String loginId = null;
        String userEmail = "kyoungsuk3254@naver.com";

        User user = Data.createUser();
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Bucket bucket = Data.createBucket(null, user, item);
        TemporaryOrder tOrder = Data.createTOrder(bucket);

        when(cacheRepository.findUserAtCache(userEmail)).thenReturn(user);
        when(temporaryOrderService.findTOrderListByUserId(user.getId())).thenReturn(Collections.singletonList(tOrder));
        //when
        LookUpBucketDto dto = bucketService.lookUpMyBucket(loginId, userEmail);
        //then
        TemporaryOrder temporaryOrder = dto.getMyBucket().get(0);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(1, dto.getMyBucket().size());
        Assertions.assertEquals(tOrder.getCount(), temporaryOrder.getCount());
        Assertions.assertEquals(tOrder.getPrice(), temporaryOrder.getPrice());
        Assertions.assertEquals(tOrder.getSize(), temporaryOrder.getSize());
        Assertions.assertEquals(MemberRole.ROLE_MEMBER, member.getRole());
    }

    @Test
    @DisplayName("장바구니를 조회할 때 필요한 데이터를 가져오는 로직, and User == null")
    public void test5() throws Exception {
        //given
        String loginId = "ks3254";
        String userEmail = null;

        User user = Data.createUser();
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Bucket bucket = Data.createBucket(null, user, item);
        TemporaryOrder tOrder = Data.createTOrder(bucket);

        when(cacheRepository.findMemberAtCache(loginId)).thenReturn(member);
        when(temporaryOrderService.findTOrderListByMemberId(member.getId())).thenReturn(Collections.singletonList(tOrder));
        //when
        LookUpBucketDto dto = bucketService.lookUpMyBucket(loginId, userEmail);
        //then
        TemporaryOrder temporaryOrder = dto.getMyBucket().get(0);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(1, dto.getMyBucket().size());
        Assertions.assertEquals(tOrder.getCount(), temporaryOrder.getCount());
        Assertions.assertEquals(tOrder.getPrice(), temporaryOrder.getPrice());
        Assertions.assertEquals(tOrder.getSize(), temporaryOrder.getSize());
        Assertions.assertEquals(MemberRole.ROLE_MEMBER, member.getRole());
    }

}
