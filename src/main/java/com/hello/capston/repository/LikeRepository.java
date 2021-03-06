package com.hello.capston.repository;

import com.hello.capston.entity.Likes;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Entity;
import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    @Query("select l from Likes l where l.item.id = :itemId")
    List<Likes> likeCount(@Param("itemId") Long itemId);

    @Query("select l from Likes l where l.member.id = :memberId and l.item.id = :itemId")
    Optional<Likes> findByMemberId(@Param("memberId") Long memberId, @Param("itemId") Long itemId);

    @Query("select l from Likes l where l.user.id = :userId and l.item.id = :itemId")
    Optional<Likes> findByUserId(@Param("userId") Long userId, @Param("itemId") Long itemId);

    @EntityGraph(attributePaths = "item")
    @Query("select l from Likes l where l.member.id = :memberId")
    List<Likes> findMyLikesByMemberId(@Param("memberId") Long memberId);

    @EntityGraph(attributePaths = "item")
    @Query("select l from Likes l where l.user.id = :userId")
    List<Likes> findMyLikesByUserId(@Param("userId") Long userId);

    @EntityGraph(attributePaths = "item")
    @Override
    Optional<Likes> findById(Long aLong);
}
