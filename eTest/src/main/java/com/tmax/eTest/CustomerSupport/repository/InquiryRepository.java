package com.tmax.eTest.CustomerSupport.repository;

import com.tmax.eTest.Common.model.support.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry,Long> {
    List<Inquiry> findAll();
}
