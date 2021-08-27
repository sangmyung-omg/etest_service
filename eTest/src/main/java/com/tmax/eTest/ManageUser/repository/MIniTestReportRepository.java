package com.tmax.eTest.ManageUser.repository;

import com.tmax.eTest.Common.model.report.MinitestReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MIniTestReportRepository extends JpaRepository<MinitestReport, Long> {
    List<MinitestReport> findByUserUuid(String user_uuid);
}
