package com.hello.capston.service;

import com.hello.capston.dto.request.payment.PaymentCompleteDto;
import com.hello.capston.entity.Delivery;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.Order;
import com.hello.capston.entity.User;
import com.hello.capston.entity.enums.OrderStatus;
import com.hello.capston.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order save(User user, Member member, Delivery delivery, PaymentCompleteDto dto) {
        Order order = new Order(member, user, delivery, LocalDateTime.now(), OrderStatus.ORDER, dto.getZipcode(), dto.getStreet(), dto.getDetailAddress());

        orderRepository.save(order);

        return order;
    }
}
