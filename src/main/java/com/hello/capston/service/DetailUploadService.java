package com.hello.capston.service;

import com.hello.capston.entity.Item;
import com.hello.capston.entity.ItemDetail;
import com.hello.capston.repository.ItemDetailRepository;
import com.hello.capston.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetailUploadService {

    private final ItemRepository itemRepository;
    private final ItemDetailRepository itemDetailRepository;

    public void detailItemUpload(String[] sizes, String[] stocks, Long itemId) {
        Item findItem = itemRepository.findById(itemId).orElse(null);
        for (int i = 0; i < sizes.length; i++) {
            ItemDetail itemDetail = new ItemDetail(Integer.parseInt(stocks[i]), sizes[i], findItem);
            itemDetailRepository.save(itemDetail);
        }
    }
}
