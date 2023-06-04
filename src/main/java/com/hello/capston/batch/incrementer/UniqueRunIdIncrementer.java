package com.hello.capston.batch.incrementer;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

import java.time.LocalDateTime;

public class UniqueRunIdIncrementer extends RunIdIncrementer {

    private static final String RUN_ID = "run.id";
    private static final Long DEFAULT_VALUE = 0L;
    private static final Long INCREMENT_VALUE = 1L;

    @Override
    public JobParameters getNext(JobParameters parameters) {
        JobParameters params = (parameters == null) ? new JobParameters() : parameters;
        return new JobParametersBuilder()
                .addLong(RUN_ID, params.getLong(RUN_ID, DEFAULT_VALUE) + INCREMENT_VALUE)
                .addString("requestDate", LocalDateTime.now().toString())
                .toJobParameters();
    }
}
