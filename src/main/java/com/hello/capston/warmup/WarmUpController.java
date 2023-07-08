package com.hello.capston.warmup;

import com.hello.capston.entity.Item;
import com.hello.capston.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WarmUpController {

    private final ItemRepository itemRepository;
    private final ReadinessInterceptor interceptor;

    @PostMapping("/warmup/item/list")
    public ResponseEntity<?> warmupItemList() {
        PageRequest page = PageRequest.of(0, 9);
        List<Item> findAllItem = itemRepository.findAllItem(page);
        itemRepository.count();

        return new ResponseEntity<>(new WarmupDto("warmup execute"), HttpStatus.ACCEPTED);
    }

    @GetMapping("/warmup/completed")
    public ResponseEntity<?> warmupComplete() {
        interceptor.setReady(true);
        log.info("health check process");
        return ResponseEntity.ok(new WarmupDto("warmup success"));
    }
}
