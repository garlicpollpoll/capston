package com.hello.capston.batch.batch;

import com.hello.capston.batch.incrementer.UniqueRunIdIncrementer;
import com.hello.capston.batch.reader.QuerydslNoOffsetPagingItemReader;
import com.hello.capston.batch.reader.expression.Expression;
import com.hello.capston.batch.reader.options.QuerydslNoOffsetNumberOptions;
import com.hello.capston.batch.reader.options.QuerydslNoOffsetStringOptions;
import com.hello.capston.entity.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import static com.hello.capston.entity.QItem.*;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MonthClickToZeroBatch {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final DataSource dataSource;

    private static final int PAGE_SIZE = 1000;

    @Bean
    public Job clickToZeroBatchJob() {
        return jobBuilderFactory
                .get("MonthClickToZeroBatchJob")
                .incrementer(new UniqueRunIdIncrementer())
                .start(clickToZeroBatchStep())
                .build();
    }

    @Bean
    public Step clickToZeroBatchStep() {
        return stepBuilderFactory
                .get("MonthClickToZeroBatchStep")
                .<Item, Item>chunk(PAGE_SIZE)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(jdbcBatchItemWriter())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public QuerydslNoOffsetPagingItemReader<Item> itemReader() {
//        QuerydslNoOffsetNumberOptions<Item, Long> options = new QuerydslNoOffsetNumberOptions<>(item.id, Expression.ASC);
        QuerydslNoOffsetStringOptions<Item> options = new QuerydslNoOffsetStringOptions<>(item.itemName, Expression.ASC);
        return new QuerydslNoOffsetPagingItemReader<>(emf, PAGE_SIZE, options, queryFactory -> {
                return queryFactory.selectFrom(item);
        });
    }

    @Bean
    public ItemProcessor<Item, Item> itemProcessor() {
        return item -> {
            return item.clickToZero();
        };
    }

    @Bean
    public JpaItemWriter<Item> jdbcBatchItemWriter() {
        return new JpaItemWriterBuilder<Item>()
                .entityManagerFactory(emf)
                .build();
    }
}
