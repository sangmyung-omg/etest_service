package com.tmax.eTest.Support.repository;

import com.tmax.eTest.Common.model.support.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("SU-NoticeRepository")
public interface NoticeRepository extends JpaRepository<Notice,Long> {
}
