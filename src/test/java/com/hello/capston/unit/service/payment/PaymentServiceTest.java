package com.hello.capston.unit.service.payment;

import com.hello.capston.dto.dto.payment.LookUpPaymentCompleteDto;
import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.OrderItemRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.iamport.PaymentService;
import com.hello.capston.unit.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    CacheRepository cacheRepository;

    @Mock
    OrderItemRepository orderItemRepository;

    @InjectMocks
    PaymentService paymentService;

    @Test
    @DisplayName("결제완료 페이지를 보여주는 로직, and Member == null")
    public void test1() throws Exception {
        //given
        String loginId = null;
        String userEmail = "kyoungsuk3254@naver.com";

        Member member = Data.createMember();
        User user = Data.createUser();
        Item item = Data.createItem(member);
        Delivery delivery = Data.createDelivery();
        Order order = Data.createOrder(null, user, delivery);
        OrderItem orderItem = Data.createOrderItem(item, order);

        when(cacheRepository.findUserAtCache(userEmail)).thenReturn(user);
        when(orderItemRepository.findOrdersByUserId(user.getId())).thenReturn(Collections.singletonList(orderItem));
        //when
        LookUpPaymentCompleteDto dto = paymentService.paymentComplete(loginId, userEmail);
        //then
        Assertions.assertEquals(1, dto.getFindOrderItem().size());
        Assertions.assertEquals(null, dto.getRole());
        Assertions.assertEquals(2000, dto.getOrderPrice());
    }

    @Test
    @DisplayName("결제완료 페이지를 보여주는 로직, and User == null")
    public void test2() throws Exception {
        //given
        String loginId = "ks3254";
        String userEmail = null;

        Member member = Data.createMember();
        User user = Data.createUser();
        Item item = Data.createItem(member);
        Delivery delivery = Data.createDelivery();
        Order order = Data.createOrder(null, user, delivery);
        OrderItem orderItem = Data.createOrderItem(item, order);

        when(cacheRepository.findMemberAtCache(loginId)).thenReturn(member);
        when(orderItemRepository.findOrdersByMemberId(member.getId())).thenReturn(Collections.singletonList(orderItem));
        //when
        LookUpPaymentCompleteDto dto = paymentService.paymentComplete(loginId, userEmail);
        //then
        Assertions.assertEquals(1, dto.getFindOrderItem().size());
        Assertions.assertEquals(MemberRole.ROLE_MEMBER, dto.getRole());
        Assertions.assertEquals(2000, dto.getOrderPrice());
    }
}
