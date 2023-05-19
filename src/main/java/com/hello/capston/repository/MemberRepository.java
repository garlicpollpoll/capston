package com.hello.capston.repository;

import com.hello.capston.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.username = :loginId")
    Optional<Member> findByLoginId(@Param("loginId") String loginId);

    @Query("select m from Member m where m.username = :loginId and m.password = :loginPw")
    Optional<Member> findByLoginIdAndLoginPw(@Param("loginId") String loginId, @Param("loginPw") String loginPw);

    boolean existsByUsername(String loginId);

    Optional<Member> findBySessionId(String sessionId);
}
