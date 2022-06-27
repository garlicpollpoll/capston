package com.hello.capston.repository;

import com.hello.capston.entity.Inquiry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    @Query("select iq from Inquiry iq order by iq.id desc")
    List<Inquiry> findAllInquiry(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"user", "member"})
    Optional<Inquiry> findById(Long aLong);
}
