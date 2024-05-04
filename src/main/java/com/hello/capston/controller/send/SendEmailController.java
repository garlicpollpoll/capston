package com.hello.capston.controller.send;

import com.hello.capston.dto.dto.uitls.SendEmailDto;
import com.hello.capston.service.email.AsyncService;
import com.hello.capston.service.email.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SendEmailController {

    private final MailService mailService;
    private final AsyncService asyncService;

    @PostMapping("/sendEmail")
    public String send(@RequestBody SendEmailDto dto, HttpSession session) {
        int num1 = (int) (Math.random() * 10);
        int num2 = (int) (Math.random() * 10);
        int num3 = (int) (Math.random() * 10);
        int num4 = (int) (Math.random() * 10);
        int num5 = (int) (Math.random() * 10);
        int num6 = (int) (Math.random() * 10);

        String checkNum = String.valueOf(num1) + String.valueOf(num2) + String.valueOf(num3) + String.valueOf(num4) + String.valueOf(num5) + String.valueOf(num6);

        asyncService.sendMailWithAsync(dto.getEmail(), "본인인증", checkNum);

        session.setAttribute("checkNumber", checkNum);

        return "redirect:/join";
    }
}
