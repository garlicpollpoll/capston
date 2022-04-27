package com.hello.capston.repository;

import com.hello.capston.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"user", "member"})
    @Query("select c from Comment c where c.item.id = :itemId")
    List<Comment> findCommentByItemId(@Param("itemId") Long itemId, Pageable pageable);

    @Query("select c from Comment c where c.item.id = :itemId")
    List<Comment> findCommentAllByItemId(@Param("itemId") Long itemId);
}
