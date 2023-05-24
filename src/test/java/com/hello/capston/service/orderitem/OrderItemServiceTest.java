package com.hello.capston.service.orderitem;

import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.DeliveryStatus;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.entity.enums.OrderStatus;
import com.hello.capston.repository.*;
import com.hello.capston.service.OrderItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @InjectMocks
    OrderItemService orderItemService;
    @Mock
    OrderItemRepository orderItemRepository;
    @Mock
    ItemDetailRepository itemDetailRepository;


    /*
    @Mock
    MemberRepository memberRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    BucketRepository bucketRepository;
    @Mock
    TemporaryOrderRepository temporaryOrderRepository;
     */

    @Test
    @DisplayName("동시성 테스트 (OrderItemService)")
    public void test() throws Exception {
        //given
        Member member = new Member(1L, "username", "password", "birth", "gender", MemberRole.ROLE_MANAGER, "email", "sessionId");
        Item item = new Item(1L, "viewName", "itemName", "brandName", "itemUrl", 10000, "uniqueCode", "category", "color", 0, member);
        Bucket bucket = new Bucket(1L, member, item, null, 0);
        TemporaryOrder tOrder1 = new TemporaryOrder(1L, bucket, 0, item.getPrice(), "XL");
        TemporaryOrder tOrder2 = new TemporaryOrder(2L, bucket, 0, item.getPrice(), "XL");
        TemporaryOrder tOrder3 = new TemporaryOrder(3L, bucket, 0, item.getPrice(), "XL");
        TemporaryOrder tOrder4 = new TemporaryOrder(4L, bucket, 0, item.getPrice(), "XL");
        ItemDetail itemDetail = new ItemDetail(1L, 100, "XL", item);
        Delivery delivery = new Delivery(1L, DeliveryStatus.READY);
        Order order = new Order(member, null, delivery, LocalDateTime.now(), OrderStatus.ORDER, "zipcode", "street", "detailAddress");
        OrderItem orderItem = new OrderItem(1L, item, order, 10000, 1);

        ArrayList<TemporaryOrder> listTOrder = new ArrayList<>();
        listTOrder.add(tOrder1);
        listTOrder.add(tOrder2);
        listTOrder.add(tOrder3);
        listTOrder.add(tOrder4);

        ArrayList<ItemDetail> listItemDetail = new ArrayList<>();
        listItemDetail.add(itemDetail);
        //when
//        Mockito.doReturn(orderItem).when(orderItemRepository).save(orderItem);
        Mockito.doReturn(orderItem).when(orderItemRepository).save(ArgumentMatchers.any(OrderItem.class));
        Mockito.doReturn(listItemDetail).when(itemDetailRepository).findByItemId(anyLong());

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(2);

        executorService.execute(() -> {
            orderItemService.saveUsingTemporaryOrder(listTOrder, order);
            System.out.println(itemDetail.getStock());
            latch.countDown();
        });
        executorService.execute(() -> {
            orderItemService.saveUsingTemporaryOrder(listTOrder, order);
            latch.countDown();
        });
        executorService.execute(() -> {
            orderItemService.saveUsingTemporaryOrder(listTOrder, order);
            latch.countDown();
        });
        latch.await();
        //then

    }

}