package com.hello.capston.unit.repository.itemdetail;

import com.hello.capston.entity.Item;
import com.hello.capston.entity.ItemDetail;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.ItemDetailRepository;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.repository.MemberRepository;
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
public class ItemDetailRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemDetailRepository itemDetailRepository;

    @Test
    @DisplayName("Item Id 로 해당 Item 의 디테일 정보를 가져오는 쿼리")
    public void test() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        ItemDetail itemDetail = Data.createItemDetail(item);
        //when
        memberRepository.save(member);
        Item saveItem = itemRepository.save(item);
        itemDetailRepository.save(itemDetail);

        List<ItemDetail> findItemDetail = itemDetailRepository.findByItemId(saveItem.getId());
        //then
        Assertions.assertNotNull(findItemDetail);
        Assertions.assertEquals(1, findItemDetail.size());
        Assertions.assertEquals(item.getItemName(), findItemDetail.get(0).getItem().getItemName());
    }

    @Test
    @DisplayName("Item Id 와 Item Detail 의 Size 를 이용해 ItemDetail 을 특정해 쿼리를 가져옴")
    public void test2() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        ItemDetail itemDetail = Data.createItemDetail(item);
        //when
        memberRepository.save(member);
        Item saveItem = itemRepository.save(item);
        ItemDetail saveItemDetail = itemDetailRepository.save(itemDetail);

        ItemDetail findItemDetail = itemDetailRepository.findByItemIdAndSize(saveItem.getId(), saveItemDetail.getSize());
        //then
        Assertions.assertNotNull(findItemDetail);
        Assertions.assertEquals(itemDetail.getSize(), findItemDetail.getSize());
    }

    @Test
    @DisplayName("findAll() 메서드 fetch join 확인")
    public void test3() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        ItemDetail itemDetail = Data.createItemDetail(item);
        //when
        memberRepository.save(member);
        itemRepository.save(item);
        itemDetailRepository.save(itemDetail);

        List<ItemDetail> findItemDetails = itemDetailRepository.findAll();
        //then
        Assertions.assertNotNull(findItemDetails);
    }
}
