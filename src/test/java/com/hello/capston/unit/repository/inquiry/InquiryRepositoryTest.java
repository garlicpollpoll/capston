package com.hello.capston.unit.repository.inquiry;

import com.hello.capston.entity.Inquiry;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.InquiryRepository;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.unit.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class InquiryRepositoryTest {

    @Autowired
    InquiryRepository inquiryRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("모든 문의 찾아오기")
    public void test() throws Exception {
        //given
        Member member = Data.createMember();
        Inquiry inquiry = Data.createInquiry(member, null);
        //when
        memberRepository.save(member);
        inquiryRepository.save(inquiry);

        List<Inquiry> findInquiry = inquiryRepository.findAllInquiry(PageRequest.of(0, 10));
        //then
        Assertions.assertNotNull(findInquiry);
    }

    @Test
    @DisplayName("문의아이디를 가지고 문의 찾기")
    public void test2() throws Exception {
        //given
        Member member = Data.createMember();
        Inquiry inquiry = Data.createInquiry(member, null);
        //when
        memberRepository.save(member);
        Inquiry saveInquiry = inquiryRepository.save(inquiry);

        Inquiry findInquiry = inquiryRepository.findById(saveInquiry.getId()).orElse(null);
        //then
        Assertions.assertNotNull(findInquiry);
        Assertions.assertEquals(inquiry.getContent(), findInquiry.getContent());
        Assertions.assertEquals(member.getUsername(), findInquiry.getMember().getUsername());
    }
}
