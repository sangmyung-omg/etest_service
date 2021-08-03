package com.tmax.eTest.Common.repository.support;

import com.tmax.eTest.Common.model.support.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long> {
}
