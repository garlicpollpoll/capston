package com.hello.capston.service.orderitem;

import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.DeliveryStatus;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.entity.enums.OrderStatus;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.OrderRepository;
import com.hello.capston.repository.TemporaryOrderRepository;
import com.hello.capston.service.OrderItemService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.RollbackException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
@Transactional
public class IsThereOptimisticException {

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    TemporaryOrderRepository temporaryOrderRepository;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OrderRepository orderRepository;

    @Test
    @Transactional
    public void test() throws Exception {
        //given
        List<TemporaryOrder> findTOrder = temporaryOrderRepository.findTemporaryOrderByMemberId(73L);
        Order findOrder = orderRepository.findById(85L).orElse(null);
        //when
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(3);

        List<Future<?>> futures = new ArrayList<>();
        Exception result = null;

        for (int i = 0; i < 3; i++) {
            try {
                Future<?> submit = executorService.submit(() -> {
                    orderItemService.saveUsingTemporaryOrder(findTOrder, findOrder);
                });
                futures.add(submit);
            } finally {
                latch.countDown();
            }
        }
        latch.await();
        //then
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause instanceof TransactionSystemException) {
                    Throwable innerCause = cause.getCause();
                    if (innerCause instanceof RollbackException) {
                        Throwable finalCause = innerCause.getCause();
                        if (finalCause != null) {
                            Assertions.assertTrue(finalCause instanceof OptimisticLockingFailureException);
                        }
                        else {
                            Assertions.fail("finalCause was null");
                        }
                    }
                    else {
                        Assertions.fail("The exception was not a RollbackException");
                    }
                }
                else {
                    Assertions.fail("The exception was not a TransactionSystemException");
                }
            }
        }
        Assertions.fail("Expected OptimisticLockingFailureException was not throw");
    }
}
