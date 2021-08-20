package com.tmax.eTest.MyPage.repository;

import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.report.DiagnosisReportKey;
import com.tmax.eTest.MyPage.dto.DiagnosisReportHistoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository("MY-DiagnosisReportRepo")
public interface DiagnosisReportRepo extends JpaRepository<DiagnosisReport, DiagnosisReportKey> {

    List<DiagnosisReport> findByUserUuid(String UserUuid);

    @Query(value = "select * FROM Diagnosis_Report WHERE USER_UUID = :USER_UUID", nativeQuery = true)
    List<DiagnosisReportHistoryDTO> findHistoryByUserUuid(String UserUuid);

}
