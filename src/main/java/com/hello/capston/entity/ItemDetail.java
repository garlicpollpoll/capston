package com.hello.capston.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ItemDetail{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_detail_id")
    private Long id;

    private int stock;
    private String size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public ItemDetail(int stock, String size, Item item) {
        this.stock = stock;
        this.size = size;
        this.item = item;
    }

    public ItemDetail(Long id, int stock, String size, Item item) {
        this.id = id;
        this.stock = stock;
        this.size = size;
        this.item = item;
    }

    public int changeStock(int count) {
        this.stock -= count;
        return this.stock;
    }

    public void changeStockToSoldOut(String message) {
        this.size = message;
    }

}
