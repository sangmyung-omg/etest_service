package com.tmax.eTest.ManageUser.repository;

import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.report.DiagnosisReportKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface DiagnosisReportRepository extends JpaRepository<DiagnosisReport, DiagnosisReportKey> {
    List<DiagnosisReport> findByUserUuid(String user_uuid);
    List<DiagnosisReport> findAllByUserUuid(String user_uuid);
}