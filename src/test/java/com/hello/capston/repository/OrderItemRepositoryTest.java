package com.hello.capston.repository;

import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.DeliveryStatus;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.entity.enums.OrderStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderItemRepositoryTest {

    @Autowired OrderItemRepository orderItemRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ItemRepository itemRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired DeliveryRepository deliveryRepository;

    @Test
    public void test() throws Exception {
        //given
        Delivery delivery = new Delivery(DeliveryStatus.READY);
        Member member = memberRepository.findById(1L).orElse(null);
        Item item = itemRepository.findById(1L).orElse(null);
        Order order = new Order(member, null, delivery, LocalDateTime.now(), OrderStatus.ORDER, "08932", "월드컵로25길 125", "101동 805호");
        OrderItem orderItem = new OrderItem(item, order, item.getPrice(), 3);
        //when
        deliveryRepository.save(delivery);
        orderRepository.save(order);
        orderItemRepository.save(orderItem);
        List<OrderItem> findOrderItem = orderItemRepository.findOrdersByMemberId(member.getId());
        //then
        Assertions.assertThat(findOrderItem.size()).isEqualTo(1);
    }
}