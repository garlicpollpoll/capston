package com.hello.capston.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/log/test/{id}")
    public String test(@PathVariable("id") Long id) throws InterruptedException {
        log.info("Process Start : " + id);
        Thread.sleep(20000);
        log.info("Process End : " + id);
        return "ok";
    }
}
