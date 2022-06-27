package com.hello.capston.repository;

import com.hello.capston.entity.TemporaryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TemporaryOrderRepository extends JpaRepository<TemporaryOrder, Long> {

    @Query("select t from TemporaryOrder t join fetch t.bucket b join fetch b.item i where t.bucket.id = :bucketId")
    TemporaryOrder findTemporaryOrderByBucketId(@Param("bucketId") Long bucketId);

    @Query("select t from TemporaryOrder t left join t.bucket b on t.bucket.id = b.id left join b.item i on b.item.id = i.id join fetch t.bucket b2 join fetch b2.item i2 where b.member.id = :memberId order by t.bucket.orders asc")
    List<TemporaryOrder> findTemporaryOrderByMemberId(@Param("memberId") Long memberId);

    @Query("select t from TemporaryOrder t left join t.bucket b on t.bucket.id = b.id left join b.item i on b.item.id = i.id join fetch t.bucket b2 join fetch b2.item i2 where b.user.id = :userId order by t.bucket.orders asc")
    List<TemporaryOrder> findTemporaryOrderByUserId(@Param("userId") Long userId);

    @Query("select t from TemporaryOrder t left join t.bucket b on t.bucket.id = b.id left join b.item i on b.item.id = i.id join fetch t.bucket b2 join fetch b2.item i2 where b.member.id = :memberId or b.user.id = :userId")
    List<TemporaryOrder> findTemporaryOrderByMemberIdOrUserId(@Param("memberId") Long memberId, @Param("userId") Long userId);
}
