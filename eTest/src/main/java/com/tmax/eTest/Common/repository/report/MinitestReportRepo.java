package com.tmax.eTest.Common.repository.report;

import com.tmax.eTest.Common.model.report.MinitestReport;
import com.tmax.eTest.Common.model.report.MinitestReportKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MinitestReportRepo extends JpaRepository<MinitestReport, MinitestReportKey>{
    
}
