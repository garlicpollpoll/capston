package com.hello.capston;

import com.hello.capston.elasticsearch.repository.ElasticItemRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
@EnableJpaRepositories(excludeFilters = @ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = ElasticItemRepository.class
))
public class CapstonApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapstonApplication.class, args);
	}

}
