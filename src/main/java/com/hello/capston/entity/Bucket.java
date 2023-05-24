package com.hello.capston.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Bucket {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bucket_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int orders;

    public Bucket(Member member, Item item, User user, int orders) {
        this.member = member;
        this.item = item;
        this.user = user;
        this.orders = orders;
    }

    public Bucket(Long id, Member member, Item item, User user, int orders) {
        this.id = id;
        this.member = member;
        this.item = item;
        this.user = user;
        this.orders = orders;
    }
}
