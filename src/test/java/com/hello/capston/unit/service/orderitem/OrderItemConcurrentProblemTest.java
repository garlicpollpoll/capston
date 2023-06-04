package com.hello.capston.unit.service.orderitem;

import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.DeliveryStatus;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.entity.enums.OrderStatus;
import com.hello.capston.repository.*;
import com.hello.capston.service.OrderItemService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.*;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class OrderItemConcurrentProblemTest {

    @Autowired
    OrderItemService orderItemService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    BucketRepository bucketRepository;
    @Autowired
    TemporaryOrderRepository temporaryOrderRepository;
    @Autowired
    ItemDetailRepository itemDetailRepository;
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    EntityManager entityManager;

    private Long bucketId;
    private Long orderId1;
    private Long orderId2;
    private Long orderId3;
    private Long itemId;
    private Long memberId;
    private Long deliveryId;
    private Long orderItemId;
    private Long tOrderId;
    private Long itemDetailId;

    @BeforeEach
    void setup() {
        Member member = new Member("username", "password", "birth", "gender", MemberRole.ROLE_MANAGER, "email", "sessionId");
        Item item = new Item("viewNameRealTest", "itemName", "brandName", "itemUrl", 10000, "uniqueCode", member, "category", "color", 0, 0);
        Delivery delivery = new Delivery(DeliveryStatus.READY);
        Order order1 = new Order(member, null, delivery, LocalDateTime.now(), OrderStatus.ORDER, "zipcode1", "street1", "detailAddress1");
        Order order2 = new Order(member, null, delivery, LocalDateTime.now(), OrderStatus.ORDER, "zipcode2", "street2", "detailAddress2");
        Order order3 = new Order(member, null, delivery, LocalDateTime.now(), OrderStatus.ORDER, "zipcode3", "street3", "detailAddress3");
        Bucket bucket = new Bucket(member, item, null, 0);
        TemporaryOrder tOrder1 = new TemporaryOrder(bucket, 1, item.getPrice(), "XL");
        ItemDetail itemDetail = new ItemDetail(1, "XL", item);

        ArrayList<TemporaryOrder> listTOrder = new ArrayList<>();
        listTOrder.add(tOrder1);

        Member saveMember = memberRepository.save(member);
        Item saveItem = itemRepository.save(item);
        Bucket saveBucket = bucketRepository.save(bucket);
        TemporaryOrder saveTOrder = temporaryOrderRepository.save(tOrder1);
        ItemDetail saveItemDetail = itemDetailRepository.save(itemDetail);
        Delivery saveDelivery = deliveryRepository.save(delivery);
        Order saveOrder1 = orderRepository.save(order1);
        Order saveOrder2 = orderRepository.save(order2);
        Order saveOrder3 = orderRepository.save(order3);

        bucketId = saveBucket.getId();
        orderId1 = saveOrder1.getId();
        orderId2 = saveOrder2.getId();
        orderId3 = saveOrder3.getId();
        itemId = saveItem.getId();
        memberId = saveMember.getId();
        tOrderId = saveTOrder.getId();
        itemDetailId = saveItemDetail.getId();
        deliveryId = saveDelivery.getId();
    }

    @Test
    public void test() throws Exception {
        //given
        ArrayList<TemporaryOrder> listTOrder = new ArrayList<>();
        TemporaryOrder tOrder = temporaryOrderRepository.findTemporaryOrderByBucketId(bucketId);
        listTOrder.add(tOrder);
        Order order1 = orderRepository.findById(orderId1).orElse(null);
        Order order2 = orderRepository.findById(orderId2).orElse(null);
        Order order3 = orderRepository.findById(orderId3).orElse(null);
        //when
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(3);
        executor.execute(() -> {
            orderItemService.saveUsingTemporaryOrder(listTOrder, order1);
            latch.countDown();
        });
        executor.execute(() -> {
            orderItemService.saveUsingTemporaryOrder(listTOrder, order2);
            latch.countDown();
        });
        executor.execute(() -> {
            orderItemService.saveUsingTemporaryOrder(listTOrder, order3);
            latch.countDown();
        });
        latch.await();
        //then
        List<OrderItem> findOrderItem = orderItemRepository.findOrdersByMemberId(memberId);
        Assertions.assertThat(findOrderItem.size()).isEqualTo(1);
    }
}
