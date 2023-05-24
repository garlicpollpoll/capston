package com.hello.capston.controller.payment;

import com.hello.capston.repository.ItemDetailRepository;
import com.hello.capston.repository.MemberWhoGetCouponRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest
class PaymentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ItemDetailRepository itemDetailRepository;
    @MockBean
    private MemberWhoGetCouponRepository memberWhoGetCouponRepository;
    @MockBean
    private DeliveryService deliveryService;
    @MockBean
    private TemporaryOrderService temporaryOrderService;
    @MockBean
    private BucketService bucketService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private UserService userService;
    @MockBean
    private OrderService orderService;
    @MockBean
    private OrderItemService orderItemService;
    @MockBean
    private AlertService alertService;
    @MockBean
    private CacheRepository cacheRepository;

    @Test
    @DisplayName("동시성 문제 테스트")
    public void test() throws Exception {
        //given

        //when

        //then
    }
}