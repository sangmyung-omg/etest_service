package com.tmax.eTest.Common.repository.report;

import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.report.DiagnosisReportKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisReportRepo extends JpaRepository<DiagnosisReport, DiagnosisReportKey>{
    
}
