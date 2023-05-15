package com.hello.capston;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
public class CapstonApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapstonApplication.class, args);
	}

}
