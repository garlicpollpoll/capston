package com.hello.capston.service;

import com.hello.capston.entity.*;
import com.hello.capston.lock.annotation.DistributedLock;
import com.hello.capston.repository.ItemDetailRepository;
import com.hello.capston.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransactionException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ItemDetailRepository itemDetailRepository;


    private boolean flag = true;
    private Map<String, String> map = new ConcurrentHashMap<>();

    @DistributedLock(key = "#key")
    public Map<String, String> saveUsingTemporaryOrder(List<TemporaryOrder> tOrder, Order order, String key) {
        for (TemporaryOrder temporaryOrder : tOrder) {
            Item findItem = temporaryOrder.getBucket().getItem();
            OrderItem orderItem = new OrderItem(findItem, order, temporaryOrder.getPrice(), temporaryOrder.getCount());

            // 아래의 코드에서 락이 걸렸기 때문에 다음으로 못넘어감
            // 때문에 최종적으로 save() 메서드가 호출되는 곳이 동시성 문제에서 해결될 수 있음
            List<ItemDetail> findItemDetail = itemDetailRepository.findByItemId(findItem.getId());

            ItemDetail itemDetailWithFilter =
                    findItemDetail.stream()
                            .filter(itemDetail -> temporaryOrder.getSize().equals(itemDetail.getSize()))
                            .findAny()
                            .orElse(new ItemDetail());

            if (itemStockIsOverZero(itemDetailWithFilter)) {
                itemDetailWithFilter.changeStock(temporaryOrder.getCount());
                itemDetailRepository.save(itemDetailWithFilter);
            }
            else {
                flag = false;
            }

            if (flag) {
                orderItemRepository.save(orderItem);
            }
            else {
                map.put("message", "상품의 재고가 없습니다.");
            }
        }
        return map;
    }

    private boolean itemStockIsOverZero(ItemDetail itemDetailWithFilter) {
        return itemDetailWithFilter.getStock() > 0;
    }
}
