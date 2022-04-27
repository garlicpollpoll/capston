package com.hello.capston.repository;

import com.hello.capston.entity.ItemDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemDetailRepository extends JpaRepository<ItemDetail, Long> {

    @EntityGraph(attributePaths = "item")
    @Query("select i from ItemDetail i where i.item.id = :itemId")
    List<ItemDetail> findByItemId(@Param("itemId") Long itemId);

    @Query("select i from ItemDetail i where i.item.id = :itemId and i.size = :size")
    ItemDetail findByItemIdAndSize(@Param("itemId") Long itemId, @Param("size") String size);
}
