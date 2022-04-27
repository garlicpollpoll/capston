package com.hello.capston.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class TemporaryOrder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temporary_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucket_id")
    private Bucket bucket;

    private int count;
    private int price;
    private String size;

    public TemporaryOrder(Bucket bucket, int count, int price, String size) {
        this.bucket = bucket;
        this.count = count;
        this.price = price;
        this.size = size;
    }

    public void changeCount(int count) {
        this.count = count;
    }
}
