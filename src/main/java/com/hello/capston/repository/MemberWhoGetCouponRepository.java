package com.hello.capston.repository;

import com.hello.capston.entity.MemberWhoGetCoupon;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberWhoGetCouponRepository extends JpaRepository<MemberWhoGetCoupon, Long> {

    @EntityGraph(attributePaths = "coupon")
    @Query("select c from MemberWhoGetCoupon c where c.user.id = :userId")
    List<MemberWhoGetCoupon> findCouponByUserId(@Param("userId") Long userId);

    @EntityGraph(attributePaths = "coupon")
    @Query("select c from MemberWhoGetCoupon c where c.member.id = :memberId")
    List<MemberWhoGetCoupon> findCouponByMemberId(@Param("memberId") Long memberId);

    @EntityGraph(attributePaths = "coupon")
    @Query("select c from MemberWhoGetCoupon c where c.user.id = :userId and c.checkUsed = :check")
    List<MemberWhoGetCoupon> findCouponByUserIdAndCheckUsed(@Param("userId") Long userId, @Param("check") int checkUsed);

    @EntityGraph(attributePaths = "coupon")
    @Query("select c from MemberWhoGetCoupon c where c.member.id = :memberId and c.checkUsed = :check")
    List<MemberWhoGetCoupon> findCouponByMemberIdAndCheckUsed(@Param("memberId") Long memberId, @Param("check") int checkUsed);

    @EntityGraph(attributePaths = "coupon")
    @Query("select c from MemberWhoGetCoupon c where c.user.id = :userId and c.coupon.couponDetail = :detail")
    Optional<MemberWhoGetCoupon> findByDetailAndUserId(@Param("detail") String detail, @Param("userId") Long userId);

    @EntityGraph(attributePaths = "coupon")
    @Query("select c from MemberWhoGetCoupon c where c.member.id = :memberId and c.coupon.couponDetail = :detail")
    Optional<MemberWhoGetCoupon> findByDetailAndMemberId(@Param("detail") String detail, @Param("memberId") Long memberId);
}
