package com.hello.capston.jwtDeprecated.repository;

import com.hello.capston.jwtDeprecated.dto.TokenDto;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<TokenDto, String> {
    public Optional<TokenDto> findByEmail(String email);
}
