package com.hello.capston.absctracts.policy.impl.utils;

import com.hello.capston.entity.ItemDetail;
import com.hello.capston.entity.TemporaryOrder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CheckStockUtils {

    public Map<String, Object> checkStockAndRedirect(TemporaryOrder findTemporaryOrder, List<ItemDetail> findItemDetail) {
        Map<String, Object> map = new HashMap<>();

        ItemDetail findItemStockIsZero = findItemDetail.stream()
                .filter((itemDetail) -> findTemporaryOrder.getSize().equals(itemDetail.getSize()))
                .filter((itemDetail) -> itemDetail.getStock() - findTemporaryOrder.getCount() < 0)
                .findAny().orElse(null);

        if (findItemStockIsZero == null) {  // 장바구니 수량 < 재고 => 정상
            map.put("checkStock", true);
        }
        else {  // 장바구니 수량 > 재고 => 막아야함
            map.put("checkStock", false);
            map.put("message", "재고가 남아있지 않습니다. 상품이름 : " + findItemStockIsZero.getItem().getViewName() + "/ 남은 재고 : " + findItemStockIsZero.getStock());
        }

        return map;
    }
}
