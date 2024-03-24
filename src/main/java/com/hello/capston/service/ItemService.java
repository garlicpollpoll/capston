package com.hello.capston.service;

import com.hello.capston.dto.form.LikeFormWithSize;
import com.hello.capston.dto.form.UploadForm;
import com.hello.capston.entity.*;
import com.hello.capston.repository.ItemDetailRepository;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.repository.LikeRepository;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemDetailRepository itemDetailRepository;

    public Item findItem(String itemId) {
        return itemRepository.findById(Long.parseLong(itemId)).orElse(new Item());
    }

    public Item saveItem(UploadForm form, String imageUrl, Member member) {
        Item item = new Item(form.getBrandName(), form.getViewName(), form.getItemName(), imageUrl, Integer.parseInt(form.getPrice()), form.getUniqueCode(), member, form.getCategory(), form.getColor(), 0, 0);

        itemRepository.save(item);

        return item;
    }

    public Item findByUniqueCode(String uniqueCode) {
        Item item = itemRepository.findByUniqueCode(uniqueCode).orElse(null);

        return item;
    }

    @Transactional
    public void changeSizeToSoldOut(List<ItemDetail> findItemDetail) {
        for (ItemDetail itemDetail : findItemDetail) {
            // n개 남음 -> 0개 남음
            if (itemDetail.getSize().contains("개남음") && itemDetail.getStock() == 0) {
                String substring = itemDetail.getSize().substring(0, 1);
                itemDetail.changeStockToSoldOut(substring + " (품절)");
            }

            // 0개 남음 -> 환불해서 n개 남음
            if (itemDetail.getSize().contains("품절") && itemDetail.getStock() > 0) {
                String substring = itemDetail.getSize().substring(0, 1);
                itemDetail.changeStockToSoldOut(substring + " (" + itemDetail.getStock() + "개남음)");
            }

            // 아무것도 안붙어있는 것들이 10개 이하로 떨어지거나 0개가 되었을 때
            if (!itemDetail.getSize().contains("품절") && !itemDetail.getSize().contains("개남음")) {
                if (itemDetail.getStock() == 0) {
                    itemDetail.changeStockToSoldOut(itemDetail.getSize() + " (품절)");
                }
                else if(itemDetail.getStock() <= 10) {
                    itemDetail.changeStockToSoldOut(itemDetail.getSize() + " (" + itemDetail.getStock() + "개남음)");
                }
            }
        }
    }

    @Transactional
    public void addClick(Item findItem) {
        findItem.addClick();
        log.info("click : {}", findItem.getClick());
    }

    public boolean checkItemStock(Item findItem, String size) {
        ItemDetail findItemDetail = itemDetailRepository.findByItemIdAndSize(findItem.getId(), size);

        if (findItemDetail.getStock() == 0) {
            return false;
        }
        else {
            return true;
        }
    }
}
