package com.hello.capston.dto.dto.like;

import com.hello.capston.entity.Item;
import com.hello.capston.entity.Likes;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import lombok.Data;

@Data
public class LikeToBucketDto {

    private Member findMember;
    private User findUser;
    private Item findItem;
    private Integer orders;
    private Likes findLike;

    public LikeToBucketDto(Member findMember, User findUser, Item findItem, Integer orders, Likes findLike) {
        this.findMember = findMember;
        this.findUser = findUser;
        this.findItem = findItem;
        this.orders = orders;
        this.findLike = findLike;
    }
}
