package com.hello.capston.dto.dto.payment;

import com.hello.capston.entity.Member;
import com.hello.capston.entity.MemberWhoGetCoupon;
import com.hello.capston.entity.TemporaryOrder;
import com.hello.capston.entity.User;
import com.hello.capston.entity.enums.MemberRole;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LookUpPaymentDto {

    private List<MemberWhoGetCoupon> findCoupon = new ArrayList<>();
    private List<TemporaryOrder> findTOrder = new ArrayList<>();
    private int orderPrice;
    private int findTOrderSize;
    private String itemName;
    private MemberRole role;
    private boolean checkStock;
    private Member member;
    private User user;
    private String message;

    public LookUpPaymentDto(List<MemberWhoGetCoupon> findCoupon, List<TemporaryOrder> findTOrder, int orderPrice,
                            int findTOrderSize, String itemName, MemberRole role, boolean checkStock,
                            Member member, User user, String message) {
        this.findCoupon = findCoupon;
        this.findTOrder = findTOrder;
        this.orderPrice = orderPrice;
        this.findTOrderSize = findTOrderSize;
        this.itemName = itemName;
        this.role = role;
        this.checkStock = checkStock;
        this.member = member;
        this.user = user;
        this.message = message;
    }

    public boolean getCheckStock() {
        return checkStock;
    }
}
