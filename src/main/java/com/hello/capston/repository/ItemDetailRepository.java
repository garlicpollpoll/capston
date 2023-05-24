package com.hello.capston.repository;

import com.hello.capston.entity.ItemDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;

public interface ItemDetailRepository extends JpaRepository<ItemDetail, Long> {

    @EntityGraph(attributePaths = "item")
    @Query("select i from ItemDetail i where i.item.id = :itemId")
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    List<ItemDetail> findByItemId(@Param("itemId") Long itemId);

    @Query("select i from ItemDetail i where i.item.id = :itemId and i.size = :size")
    ItemDetail findByItemIdAndSize(@Param("itemId") Long itemId, @Param("size") String size);

    @Override
    @EntityGraph(attributePaths = "item")
    List<ItemDetail> findAll();
}
