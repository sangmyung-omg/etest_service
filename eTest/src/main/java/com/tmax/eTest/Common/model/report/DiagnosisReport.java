package com.tmax.eTest.Common.model.report;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.tmax.eTest.Common.model.user.UserMaster;

import lombok.Data;

@Data
@Entity
@Table(name="DIAGNOSIS_REPORT")
@IdClass(DiagnosisReportKey.class)
public class DiagnosisReport {
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
	
	@ManyToOne
	@JoinColumn(name="userUuid", insertable = false, updatable = false)
	private UserMaster user;
}
