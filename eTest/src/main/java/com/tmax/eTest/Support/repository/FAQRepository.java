package com.tmax.eTest.Support.repository;

import com.tmax.eTest.Common.model.support.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("SU-FQARepository")
public interface FAQRepository extends JpaRepository<FAQ, Long> {
}
