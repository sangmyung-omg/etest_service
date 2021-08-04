package com.tmax.eTest.Support.repository;

import com.tmax.eTest.Common.model.support.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("SU-FAQRepository")
public interface FAQRepository extends JpaRepository<FAQ, Long> {
}
