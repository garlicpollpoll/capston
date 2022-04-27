package com.hello.capston.controller.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hello.capston.dto.dto.PaymentCompleteDto;
import com.hello.capston.entity.Bucket;
import com.hello.capston.entity.Item;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.service.BucketService;
import com.hello.capston.service.ItemService;
import com.hello.capston.service.MemberService;
import com.hello.capston.service.TemporaryOrderService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    TemporaryOrderService temporaryOrderService;
    @Autowired
    BucketService bucketService;
    @Autowired
    MemberService memberService;
    @Autowired
    ItemRepository itemRepository;

    @Test
    public void test() throws Exception {
        Runnable userA = () -> {
            Member findMember = memberService.findMemberById(1L);
            Item findItem = itemRepository.findById(3L).orElse(null);

            Bucket bucket = bucketService.save(findMember, null, findItem, 0);
            temporaryOrderService.save(bucket, findItem.getPrice(), "L (2개남음)");

            String content = null;

            try {
                content = objectMapper.writeValueAsString(new PaymentCompleteDto("1", "", "03985", "월드컵로25길 125", "101동 805호"));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            try {
                mockMvc.perform(
                        post("/paymentComplete")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content))
                        .andExpect(status().isOk())
                        .andDo(print());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}