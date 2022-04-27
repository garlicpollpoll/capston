package com.hello.capston.entity;

import com.hello.capston.entity.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String zipcode;
    private String street;
    private String detailAddress;

    public Order(Member member, User user, Delivery delivery, LocalDateTime orderDate, OrderStatus status, String zipcode, String street, String detailAddress) {
        this.member = member;
        this.user = user;
        this.delivery = delivery;
        this.orderDate = orderDate;
        this.status = status;
        this.street = street;
        this.zipcode = zipcode;
        this.detailAddress = detailAddress;
    }
}
