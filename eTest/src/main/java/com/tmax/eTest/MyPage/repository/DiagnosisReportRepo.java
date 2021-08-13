package com.tmax.eTest.MyPage.repository;

import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.report.DiagnosisReportKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository("MY-DiagnosisReportRepo")
public interface DiagnosisReportRepo extends JpaRepository<DiagnosisReport, DiagnosisReportKey> {
    List<DiagnosisReport> findAllByUserUuid(String UserUuid);
}
