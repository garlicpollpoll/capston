package com.hello.capston.repository;

import com.hello.capston.entity.Order;
import com.hello.capston.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select oi from OrderItem oi join fetch oi.order o join fetch oi.item i where o.member.id = :memberId")
    List<OrderItem> findOrdersByMemberId(@Param("memberId") Long memberId);

    @Query("select oi from OrderItem oi join fetch oi.order o join fetch oi.item i where o.user.id = :userId")
    List<OrderItem> findOrdersByUserId(@Param("userId") Long userId);
}
