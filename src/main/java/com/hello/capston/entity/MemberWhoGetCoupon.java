package com.hello.capston.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class MemberWhoGetCoupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_who_get_coupon_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private int checkUsed;

    public MemberWhoGetCoupon(User user, Member member, Coupon coupon, int checkUsed) {
        this.user = user;
        this.member = member;
        this.coupon = coupon;
        this.checkUsed = checkUsed;
    }

    public void changeCheckUsedToOne() {
        this.checkUsed = 1;
    }
}
