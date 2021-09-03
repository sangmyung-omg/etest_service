package com.tmax.eTest.Admin.dashboard.dto;
import java.sql.Timestamp;

public interface DiagnosisReportGetterDTO {
    String getDiagnosisId();
    Timestamp getDiagnosisDate();
    Float getGiScore();
    Integer getRiskScore();
    Integer getInvestScore();
    Integer getKnowledgeScore();
}
