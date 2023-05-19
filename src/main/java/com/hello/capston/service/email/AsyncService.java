package com.hello.capston.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class AsyncService {

    private final MailService mailService;

    public void sendMailWithAsync(String to, String subject, String checkNum) {
        System.out.println("[AyncCall_1 :: " + Thread.currentThread().getName() + "]");
        mailService.sendMail(to, subject, checkNum);
    }
}
