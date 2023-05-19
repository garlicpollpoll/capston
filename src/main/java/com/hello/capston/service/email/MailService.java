package com.hello.capston.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Async("mailExecutor")
    public void sendMail(String to, String subject, String checkNum) {
        System.out.println("[sendMail :: " + Thread.currentThread().getName() + "]");

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("kyoungsuk3254@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(checkNum);
        };

        mailSender.send(messagePreparator);
    }
}
