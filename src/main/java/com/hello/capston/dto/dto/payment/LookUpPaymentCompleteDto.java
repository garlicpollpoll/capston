package com.hello.capston.dto.dto.payment;

import com.hello.capston.entity.OrderItem;
import com.hello.capston.entity.enums.MemberRole;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LookUpPaymentCompleteDto {

    private List<OrderItem> findOrderItem = new ArrayList<>();
    private MemberRole role;
    private int orderPrice;

    public LookUpPaymentCompleteDto(List<OrderItem> findOrderItem, MemberRole role, int orderPrice) {
        this.findOrderItem = findOrderItem;
        this.role = role;
        this.orderPrice = orderPrice;
    }
}
