package com.tmax.eTest.MyPage.dto;

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
public class MiniTestReportHistoryDTO {
    private String minitestId;
    private Timestamp minitestDate;
    private Float avgUkMastery;
    public static MiniTestReportHistoryDTO toDto(MinitestReport minitestReport) {
        return MiniTestReportHistoryDTO.builder()
                .minitestId(minitestReport.getMinitestId())
                .avgUkMastery(minitestReport.getAvgUkMastery())
                .minitestDate(minitestReport.getMinitestDate())
                .build();
    }
    public static List<MiniTestReportHistoryDTO> toDtoList(List<MinitestReport> minitestReport) {
        return minitestReport
                .stream()
                .map(MiniTestReportHistoryDTO::toDto)
                .collect(Collectors.toList());
    }
}
