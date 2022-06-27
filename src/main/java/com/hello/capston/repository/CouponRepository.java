package com.hello.capston.repository;

import com.hello.capston.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCode(String code);

    @Query("select c from Coupon c where c.couponDetail = :detail")
    Optional<Coupon> findByDetail(@Param("detail") String couponDetail);
}
