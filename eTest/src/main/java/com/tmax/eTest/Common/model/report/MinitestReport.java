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
@Table(name="MINITEST_REPORT")
@IdClass(MinitestReportKey.class)
public class MinitestReport {
	@Id
	private String minitestId;
	private String userUuid;
	private Float avgUkMastery;
	private Integer setNum;
	private Integer correctNum;
	private Integer wrongNum;
	private Integer dunnoNum;
	private Timestamp minitestDate;
	
	@ManyToOne
	@JoinColumn(name="userUuid", insertable = false, updatable = false)
	private UserMaster user;
}
