package com.tmax.eTest.Support.repository;

import com.tmax.eTest.Common.model.support.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("SU-InquiryRepository")
public interface InquiryRepository extends JpaRepository<Inquiry,Long> {

}
