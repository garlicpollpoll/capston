package com.hello.capston.warmup;

import com.hello.capston.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ClassLoaderWarmup implements ApplicationRunner {

    private final ItemRepository itemRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        itemRepository.findAllItem(PageRequest.of(0, 9));
        itemRepository.count();
    }
}
