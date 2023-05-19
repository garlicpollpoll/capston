package com.hello.capston.controller;

import com.hello.capston.service.email.AsyncService;
import com.hello.capston.service.email.MailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpSession;

//@WebMvcTest(
//        controllers = SendEmailController.class,
//        excludeAutoConfiguration = {SecurityAutoConfiguration.class, RedisConfig.class},
//        excludeFilters = {
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
//        }
//)
@SpringBootTest
public class MailControllerTest {

    @Autowired
    private MailService mailService;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private HttpSession session;

    private String email = "kyoungsuk3254@naver.com";
    private String subject = "본인인증";


    @Test
    @DisplayName("동기 네트워킹일 때 속도 테스트")
    public void test1() {
        mailService.sendMail(email, subject, "123456");
    }

    @Test
    public void test2() throws Exception {
        asyncService.sendMailWithAsync(email, subject, "123456");
    }
}
