package com.tmax.eTest.Common.model.report;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.tmax.eTest.Common.model.user.UserMaster;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="DIAGNOSIS_REPORT")
//@IdClass(DiagnosisReportKey.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisReport {
	@Id
	private String diagnosisId;
	private String userUuid;
	private Integer giScore;
	
	private Integer riskScore;
	private Integer riskProfileScore;
	private Integer riskTracingScore;
	private Integer riskLevelScore;
	private Integer riskCapaScore;
	
	private Integer investScore;
	private Integer investProfileScore;
	private Integer investTracingScore;
	
	private Integer knowledgeScore;
	private Integer knowledgeCommonScore;
	private Integer knowledgeTypeScore;
	private Integer knowledgeChangeScore;
	private Integer knowledgeSellScore;
	
	private Float avgUkMastery;
	private String userMbti;
	private Integer investItemNum;
	private Integer investPeriod;
	private Integer stockRatio;
	private Timestamp diagnosisDate;
	
	private String recommendBasicList;
	private String recommendTypeList;
	private String recommendAdvancedList;
	
// error 및 미사용
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name="userUuid", insertable = false, updatable = false)
//	private UserMaster user;
}
