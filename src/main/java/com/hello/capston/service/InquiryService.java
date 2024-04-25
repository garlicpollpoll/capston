package com.hello.capston.service;

import com.hello.capston.entity.Inquiry;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import com.hello.capston.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public void saveInquiry(Member member, User user, String writeDate, String content, String title) {
        Inquiry inquiry = new Inquiry(member, user, title, writeDate, content);
        inquiryRepository.save(inquiry);
    }

    public void deleteInquiry(Inquiry inquiry) {
        inquiryRepository.delete(inquiry);
    }
}
