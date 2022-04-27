package com.hello.capston.repository;

import com.hello.capston.entity.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BucketRepository extends JpaRepository<Bucket, Long> {

    @Query("select b from Bucket b join fetch b.item i where b.member.id = :memberId order by b.orders asc")
    List<Bucket> findByMemberId(@Param("memberId") Long memberId);

    @Query("select b from Bucket b join fetch b.item i where b.user.id = :userId order by b.orders asc")
    List<Bucket> findByUserId(@Param("userId") Long userId);

    @Query("select sum(i.price) from Bucket b left join b.item i on b.item.id = i.id left join b.member m on b.member.id = m.id where m.id = :memberId")
    Optional<Integer> findTotalAmountByMemberId(@Param("memberId") Long memberId);

    @Query("select sum(i.price) from Bucket b left join b.item i on b.item.id = i.id left join b.user u on b.user.id = u.id where u.id = :userId")
    Optional<Integer> findTotalAmountByUserId(@Param("userId") Long userId);

}
