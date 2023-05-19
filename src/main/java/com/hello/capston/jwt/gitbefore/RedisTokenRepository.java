package com.hello.capston.jwt.gitbefore;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RedisTokenRepository extends CrudRepository<RedisAndSession, String> {

    Optional<RedisAndSession> findBySessionId(String sessionId);
}
