package com.tmax.eTest.CustomerSupport.repository;

import com.tmax.eTest.Common.model.support.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry,Long> {
    List<Inquiry> findAll();
    Optional<Inquiry> findById(Long id);
}
