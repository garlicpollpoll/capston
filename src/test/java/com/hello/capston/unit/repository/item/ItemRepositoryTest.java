package com.hello.capston.unit.repository.item;

import com.hello.capston.entity.Item;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.unit.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("모든 상품 페이징 쿼리")
    public void test() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        //when
        memberRepository.save(member);
        itemRepository.save(item);

        List<Item> findItems = itemRepository.findAllItem(PageRequest.of(0, 10));
        //then
        Assertions.assertNotNull(findItems);
        Assertions.assertEquals(10, findItems.size());
    }

    @Test
    @DisplayName("조회수를 기반으로 높은 것부터 차례대로 보여주기")
    public void test2() throws Exception {
        //given

        //when
        List<Item> findItems = itemRepository.findAllItemByCount(PageRequest.of(0, 10));
        //then
        Assertions.assertNotNull(findItems);
        Assertions.assertEquals(10, findItems.size());
    }

    @Test
    @DisplayName("유니크 코드를 이용해 Item 특정해서 가져오기")
    public void test3() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        //when
        memberRepository.save(member);
        Item saveItem = itemRepository.save(item);

        Item findItem = itemRepository.findByUniqueCode(saveItem.getUniqueCode()).orElse(null);
        //then
        Assertions.assertNotNull(findItem);
        Assertions.assertEquals(item.getItemName(), findItem.getItemName());
    }

    @Test
    @DisplayName("ItemName 으로 Item 들 가져오기")
    public void test4() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        //when
        memberRepository.save(member);
        Item saveItem = itemRepository.save(item);

        List<Item> findItems = itemRepository.findByItemName(saveItem.getItemName());
        //then
        Assertions.assertNotNull(findItems);
        Assertions.assertEquals(1, findItems.size());
    }

    @Test
    @DisplayName("카테고리로 상품 찾기 (페이징)")
    public void test5() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        //when
        memberRepository.save(member);
        Item saveItem = itemRepository.save(item);

        List<Item> findItems = itemRepository.findByCategory("ca", PageRequest.of(0, 10));
        //then
        Assertions.assertNotNull(findItems);
        Assertions.assertEquals(10, findItems.size());
    }

    @Test
    @DisplayName("카테고리로 상품 찾기 (모두)")
    public void test6() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        //when
        memberRepository.save(member);
        Item saveItem = itemRepository.save(item);

        List<Item> findItems = itemRepository.findByCategoryAll("ca");
        //then
        Assertions.assertNotNull(findItems);
    }
}
