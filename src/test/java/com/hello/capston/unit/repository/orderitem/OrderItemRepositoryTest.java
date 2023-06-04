package com.hello.capston.unit.repository.orderitem;

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
public class OrderItemRepositoryTest {

    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Member Id 를 이용해 내 주문 내역 조회하기")
    public void test() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Delivery delivery = Data.createDelivery();
        Order order = Data.createOrder(member, null, delivery);
        OrderItem orderItem = Data.createOrderItem(item, order);
        //when
        Member saveMember = memberRepository.save(member);
        itemRepository.save(item);
        deliveryRepository.save(delivery);
        orderRepository.save(order);
        orderItemRepository.save(orderItem);

        List<OrderItem> findOrderItem = orderItemRepository.findOrdersByMemberId(saveMember.getId());
        //then
        Assertions.assertNotNull(findOrderItem);
        Assertions.assertEquals(1, findOrderItem.size());
        Assertions.assertEquals(item.getItemName(), findOrderItem.get(0).getItem().getItemName());
        Assertions.assertEquals(order.getDetailAddress(), findOrderItem.get(0).getOrder().getDetailAddress());
    }

    @Test
    @DisplayName("User Id 를 이용해 내 주문내역 조회하기")
    public void test2() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Delivery delivery = Data.createDelivery();
        User user = Data.createUser();
        Order order = Data.createOrder(null, user, delivery);
        OrderItem orderItem = Data.createOrderItem(item, order);
        //when
        memberRepository.save(member);
        User saveUser = userRepository.save(user);
        itemRepository.save(item);
        deliveryRepository.save(delivery);
        orderRepository.save(order);
        orderItemRepository.save(orderItem);

        List<OrderItem> findOrderItem = orderItemRepository.findOrdersByUserId(saveUser.getId());
        //then
        Assertions.assertNotNull(findOrderItem);
        Assertions.assertEquals(1, findOrderItem.size());
        Assertions.assertEquals(item.getItemName(), findOrderItem.get(0).getItem().getItemName());
        Assertions.assertEquals(order.getDetailAddress(), findOrderItem.get(0).getOrder().getDetailAddress());
    }
}
