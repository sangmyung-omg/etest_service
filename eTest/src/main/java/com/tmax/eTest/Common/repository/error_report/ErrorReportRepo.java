package com.tmax.eTest.Common.repository.error_report;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tmax.eTest.Common.model.error_report.ErrorReport;

public interface ErrorReportRepo extends JpaRepository<ErrorReport, Long>{
    
}