package com.hello.capston.controller.send;

import com.hello.capston.dto.dto.SendEmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SendEmailController {

    private final JavaMailSender javaMailSender;

    @PostMapping("/sendEmail")
    public String send(@RequestBody SendEmailDto dto, HttpSession session) {
        int num1 = (int) (Math.random() * 10);
        int num2 = (int) (Math.random() * 10);
        int num3 = (int) (Math.random() * 10);
        int num4 = (int) (Math.random() * 10);
        int num5 = (int) (Math.random() * 10);
        int num6 = (int) (Math.random() * 10);

        String checkNum = String.valueOf(num1) + String.valueOf(num2) + String.valueOf(num3) + String.valueOf(num4) + String.valueOf(num5) + String.valueOf(num6);

        ArrayList<String> toUserList = new ArrayList<>();

        toUserList.add(dto.getEmail());

        int toUserSize = toUserList.size();

        SimpleMailMessage simpleMessage = new SimpleMailMessage();

        simpleMessage.setTo((String[]) toUserList.toArray(new String[toUserSize]));

        simpleMessage.setSubject("본인인증");

        simpleMessage.setText(checkNum);

        javaMailSender.send(simpleMessage);

        session.setAttribute("checkNumber", checkNum);

        return "redirect:/join";
    }
}
