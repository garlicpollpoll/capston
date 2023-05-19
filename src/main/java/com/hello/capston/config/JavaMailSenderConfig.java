package com.hello.capston.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailSenderConfig {

    private String protocol = "smtp";
    private boolean auth = true;
    private boolean starttls = true;
    private boolean debug = true;
    private String host = "smtp.gmail.com";
    private int port = 587;
    private String username = "kyoungsuk3254@gmail.com";
    private String password = "cnlgfpagxudlgjzl";
    private String encoding = "UTF-8";

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        Properties properties = new Properties();

        // properties
        properties.put("mail.transport.protocol", protocol);
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", starttls);
        properties.put("mail.smtp.debug", debug);

        // mail sender
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setJavaMailProperties(properties);
        javaMailSender.setDefaultEncoding(encoding);
        javaMailSender.setProtocol(protocol);
        return javaMailSender;
    }
}
