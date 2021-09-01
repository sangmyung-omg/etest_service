package com.tmax.eTest.MyPage.dto;


import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.report.MinitestReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisReportHistoryDTO {
    private String diagnosisId;
    private Integer giScore;
    private Timestamp diagnosisDate;
    public static DiagnosisReportHistoryDTO toDto(DiagnosisReport diagnosisReport) {
        return DiagnosisReportHistoryDTO.builder()
                .diagnosisDate(diagnosisReport.getDiagnosisDate())
                .giScore(diagnosisReport.getGiScore())
                .diagnosisId(diagnosisReport.getDiagnosisId())
                .build();
    }
    public static List<DiagnosisReportHistoryDTO> toDtoList(List<DiagnosisReport> diagnosisReport) {
        return diagnosisReport
                .stream()
                .map(DiagnosisReportHistoryDTO::toDto)
                .collect(Collectors.toList());
    }

}
