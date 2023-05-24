package com.hello.capston.service;

import com.hello.capston.entity.*;
import com.hello.capston.repository.ItemDetailRepository;
import com.hello.capston.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransactionException;
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

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Map<String, String> saveUsingTemporaryOrder(List<TemporaryOrder> tOrder, Order order) {
        for (TemporaryOrder temporaryOrder : tOrder) {
            Item findItem = temporaryOrder.getBucket().getItem();
            OrderItem orderItem = new OrderItem(findItem, order, temporaryOrder.getPrice(), temporaryOrder.getCount());

            // 아래의 코드에서 락이 걸렸기 때문에 다음으로 못넘어감
            // 때문에 최종적으로 save() 메서드가 호출되는 곳이 동시성 문제에서 해결될 수 있음
            List<ItemDetail> findItemDetail = itemDetailRepository.findByItemId(findItem.getId());

            try {
                for (ItemDetail itemDetail : findItemDetail) {
                    if (temporaryOrder.getSize().equals(itemDetail.getSize()) && itemDetail.getStock() > 0) {
                        itemDetail.changeStock(temporaryOrder.getCount());
                        itemDetailRepository.save(itemDetail);
                    }
                    else {
                        flag = false;
                    }
                }
                if (flag) {
                    orderItemRepository.save(orderItem);
                }
                else {
                    throw new TransactionException("상품의 재고가 없습니다.");
                }
            } catch (TransactionException e) {
                map.put("message", "상품의 재고가 없습니다.");
            }
        }
        return map;
    }
}
