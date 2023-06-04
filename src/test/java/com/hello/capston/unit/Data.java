package com.hello.capston.unit;

import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.DeliveryStatus;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.entity.enums.OrderStatus;
import com.hello.capston.entity.enums.Role;
import org.aspectj.weaver.ast.Or;

import java.time.LocalDateTime;

public class Data {

    public static Item createItem(Member member) {
        return new Item("brandName", "viewName", "itemName", "url", 1000, "code", member, "category", "color", 0, 0);
    }

    public static Bucket createBucket(Member member, User user, Item item) {
        return new Bucket(member, item, user, 1);
    }

    public static TemporaryOrder createTOrder(Bucket bucket) {
        return new TemporaryOrder(bucket, 1, 1000, "XL");
    }

    public static MemberWhoGetCoupon createMemberWhoGetCoupon(User user, Member member, Coupon coupon) {
        return new MemberWhoGetCoupon(user, member, coupon, 0);
    }

    public static Member createMember() {
        return new Member("usernameMember", "password", "birth", "gender", MemberRole.ROLE_MEMBER, "email", "session");
    }

    public static User createUser() {
        return new User("name", "email", "picture", Role.USER, "key", "session");
    }

    public static Coupon createCoupon() {
        return new Coupon("image", "detail", 10, "date", "code");
    }

    public static ItemDetail createItemDetail(Item item) {
        return new ItemDetail(1, "XL", item);
    }

    public static Likes createLike(Member member, User user, Item item) {
        return new Likes(member, user, item, "좋아요", "XL");
    }

    public static OrderItem createOrderItem(Item item, Order order) {
        return new OrderItem(item, order, 1000, 2);
    }

    public static Order createOrder(Member member, User user, Delivery delivery) {
        return new Order(member, user, delivery, LocalDateTime.now(), OrderStatus.ORDER, "zipcode", "street", "address");
    }

    public static Delivery createDelivery() {
        return new Delivery(DeliveryStatus.READY);
    }

    public static Comment createComment(Member member, User user, Item item) {
        return new Comment(member, user, item, "comment", "url");
    }

    public static Inquiry createInquiry(Member member, User user) {
        return new Inquiry(member, user, "title", "date", "content");
    }
}
