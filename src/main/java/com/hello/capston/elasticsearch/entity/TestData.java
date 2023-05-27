package com.hello.capston.elasticsearch.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "test_item")
@Getter
@NoArgsConstructor
public class TestData {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String view_name;

    @Field(type = FieldType.Text)
    private String item_name;

    @Field(type = FieldType.Integer)
    private int price;

    @PersistenceConstructor
    public TestData(String view_name, String item_name, int price) {
        this.view_name = view_name;
        this.item_name = item_name;
        this.price = price;
    }
}
