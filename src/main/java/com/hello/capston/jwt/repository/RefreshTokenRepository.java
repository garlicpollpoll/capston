package com.hello.capston.jwt.repository;

import com.hello.capston.jwt.dto.TokenDto;
import com.hello.capston.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<TokenDto, String> {
    public Optional<TokenDto> findByEmail(String email);
}
