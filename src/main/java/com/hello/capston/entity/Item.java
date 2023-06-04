package com.hello.capston.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceConstructor;

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

    private String brandName;

    private String itemUrl;

    private int price;

    private String uniqueCode;

    private String category;

    private String color;

    private int click;

    private int monthClick;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Item(String itemName, String itemUrl) {
        this.itemName = itemName;
        this.itemUrl = itemUrl;
    }

    public Item(String brandName, String viewName, String itemName, String itemUrl, int price, String uniqueCode, Member member, String category, String color, int click, int monthClick) {
        this.brandName = brandName;
        this.viewName = viewName;
        this.itemName = itemName;
        this.itemUrl = itemUrl;
        this.price = price;
        this.uniqueCode = uniqueCode;
        this.member = member;
        this.category = category;
        this.color = color;
        this.click = click;
        this.monthClick = monthClick;
    }

    public Item(Long id, String viewName, String itemName, String brandName, String itemUrl, int price, String uniqueCode, String category, String color, int click, Member member) {
        this.id = id;
        this.viewName = viewName;
        this.itemName = itemName;
        this.brandName = brandName;
        this.itemUrl = itemUrl;
        this.price = price;
        this.uniqueCode = uniqueCode;
        this.category = category;
        this.color = color;
        this.click = click;
        this.member = member;
    }

    @PersistenceConstructor
    public Item(String viewName, String itemName, String brandName) {
        this.viewName = viewName;
        this.itemName = itemName;
        this.brandName = brandName;
    }

    public void addClick() {
        this.click += 1;
    }

    public Item clickToZero() {
        this.monthClick = 0;
        return this;
    }
}
