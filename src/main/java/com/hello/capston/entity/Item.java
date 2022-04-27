package com.hello.capston.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String viewName;

    private String itemName;

    private String itemUrl;

    private int price;

    private String uniqueCode;

    private String category;

    private String color;

    private int click;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Item(String itemName, String itemUrl) {
        this.itemName = itemName;
        this.itemUrl = itemUrl;
    }

    public Item(String viewName, String itemName, String itemUrl, int price, String uniqueCode, Member member, String category, String color, int click) {
        this.viewName = viewName;
        this.itemName = itemName;
        this.itemUrl = itemUrl;
        this.price = price;
        this.uniqueCode = uniqueCode;
        this.member = member;
        this.category = category;
        this.color = color;
        this.click = click;
    }

    public void addClick() {
        this.click += 1;
    }
}
