package com.hello.capston.absctracts.policy.impl.utils;

import com.hello.capston.entity.Bucket;
import com.hello.capston.entity.ItemDetail;
import com.hello.capston.entity.TemporaryOrder;
import com.hello.capston.repository.BucketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CheckStockUtils {

    public Map<String, Object> checkStockAndRedirect(TemporaryOrder findTemporaryOrder, List<ItemDetail> findItemDetail) {
        Map<String, Object> map = new HashMap<>();

        map.put("checkStock", true);

        findItemDetail.stream()
                .filter((itemDetail) -> findTemporaryOrder.getSize().equals(itemDetail.getSize()))
                .filter((itemDetail) -> itemDetail.getStock() - findTemporaryOrder.getCount() < 0)
                .findAny().ifPresent((itemDetail) -> {
                    map.put("checkStock", false);
                    map.put("message", "재고가 남아있지 않습니다. 상품이름 : " + itemDetail.getItem().getViewName() + "/ 남은 재고 : " + itemDetail.getStock());
                });

        return map;
    }
}
