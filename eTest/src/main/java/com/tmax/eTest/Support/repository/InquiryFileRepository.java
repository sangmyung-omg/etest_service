package com.tmax.eTest.Support.repository;

import com.tmax.eTest.Common.model.support.Inquiry_file;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("SU-InquiryFileRepository")
public interface InquiryFileRepository extends JpaRepository<Inquiry_file,Long> {
}
