package com.hello.capston.service;

import com.hello.capston.entity.*;
import com.hello.capston.repository.ItemDetailRepository;
import com.hello.capston.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ItemDetailRepository itemDetailRepository;

    @Transactional
    public void saveUsingTemporaryOrder(List<TemporaryOrder> tOrder, Order order) {
        for (TemporaryOrder temporaryOrder : tOrder) {
            Item findItem = temporaryOrder.getBucket().getItem();
            OrderItem orderItem = new OrderItem(findItem, order, temporaryOrder.getPrice(), temporaryOrder.getCount());

            List<ItemDetail> findItemDetail = itemDetailRepository.findByItemId(findItem.getId());

            for (ItemDetail itemDetail : findItemDetail) {
                if (temporaryOrder.getSize().equals(itemDetail.getSize())) {
                    itemDetail.changeStock(temporaryOrder.getCount());
                }
            }

            orderItemRepository.save(orderItem);
        }
    }
}
