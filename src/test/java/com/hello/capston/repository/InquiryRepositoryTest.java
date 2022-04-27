package com.hello.capston.repository;

import com.hello.capston.entity.Inquiry;
import com.hello.capston.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InquiryRepositoryTest {

    @Autowired InquiryRepository inquiryRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    public void test() throws Exception {
        //given
        LocalDateTime date = LocalDateTime.now();
        Member member = memberRepository.findById(1L).orElse(null);
        Inquiry inquiry = new Inquiry(member, null, "title", date.toString().substring(0, 10), "content");
        //when
        inquiryRepository.save(inquiry);
        PageRequest page = PageRequest.of(0, 3);
        List<Inquiry> findAll = inquiryRepository.findAllInquiry(page);
        //then
        Assertions.assertThat(findAll.size()).isEqualTo(1);
    }

}