package com.hello.capston.controller.bucket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hello.capston.dto.request.ChangeCountDto;
import com.hello.capston.service.BucketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChangeItemCountController {

    private final BucketService bucketService;

    /**
     * 장바구니 수량 증가
     * @param dto
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/changeCount")
    @ResponseBody
    @Transactional
    public Map<String, String> changeCount(@RequestBody ChangeCountDto dto) throws JsonProcessingException {
        Map<String, String> map = bucketService.isBucketStockZero(dto);

        return map;
    }
}
