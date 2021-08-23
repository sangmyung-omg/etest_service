package com.tmax.eTest.MyPage.dto;

import com.tmax.eTest.Common.model.user.UserMaster;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Data
@Builder
public class DiagnosisReportDto {
    @Id
    private String userUuid;
    private Float giScore;
    private Integer riskScore;
    private Integer investScore;
    private Integer knowledgeScore;
    private Float avgUkMastery;
    private String userMbti;
    private Integer investItemNum;
    private Integer stockRatio;
    @Id
    private Timestamp diagnosisDate;

}
