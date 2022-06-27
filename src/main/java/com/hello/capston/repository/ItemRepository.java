package com.hello.capston.repository;

import com.hello.capston.entity.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Entity;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i order by i.id desc")
    List<Item> findAllItem(Pageable pageable);

    @Query("select i from Item  i order by i.click desc")
    List<Item> findAllItemByCount(Pageable pageable);

    @Query("select i from Item i where i.category = :category order by i.click desc")
    List<Item> findMostClickByCategory(Pageable pageable, @Param("category") String category);

    @Query("select i from Item i where i.uniqueCode = :uniqueCode")
    Optional<Item> findByUniqueCode(@Param("uniqueCode") String uniqueCode);

    @Query("select i from Item i where i.itemName = :itemName")
    List<Item> findByItemName(@Param("itemName") String itemName);

    @Query("select i from Item i where i.category like concat('%', :category, '%')")
    List<Item> findByCategory(@Param("category") String category, Pageable pageable);

    @Query("select i from Item i where i.category like concat('%', :category, '%') ")
    List<Item> findByCategoryAll(@Param("category") String category);

    @Query("select i from Item i where i.itemName = :itemName")
    List<Item> itemListByItemName(@Param("itemName") String itemName);
}
