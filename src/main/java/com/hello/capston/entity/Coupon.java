package com.hello.capston.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Coupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    private String couponImage;

    private String couponDetail;

    private int percentage;

    private String expirationDate;

    private String code;

    public Coupon(String couponImage, String couponDetail, int percentage, String expirationDate, String code) {
        this.couponImage = couponImage;
        this.couponDetail = couponDetail;
        this.percentage = percentage;
        this.expirationDate = expirationDate;
        this.code = code;
    }
}
