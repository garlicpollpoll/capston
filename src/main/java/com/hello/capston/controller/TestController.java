package com.hello.capston.controller;

import com.hello.capston.batch.batch.MonthClickToZeroBatch;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final JobLauncher jobLauncher;
    private final Job clickToZeroBatchJob;

    @GetMapping("/test")
    public String test(HttpSession session) {
        try {
            jobLauncher.run(clickToZeroBatchJob, new JobParametersBuilder().addString("requestDate", LocalDateTime.now().toString()).toJobParameters());
        } catch (JobExecutionAlreadyRunningException e) {
            throw new RuntimeException(e.getCause());
        } catch (JobRestartException e) {
            throw new RuntimeException(e.getCause());
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e.getCause());
        } catch (JobParametersInvalidException e) {
            throw new RuntimeException(e.getCause());
        }
        return "success";
    }
}
