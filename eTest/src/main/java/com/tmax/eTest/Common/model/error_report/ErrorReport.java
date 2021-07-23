package com.tmax.eTest.Common.model.error_report;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.user.UserMaster;

import lombok.Data;

@Data
@Entity
@Table(name="ERROR_REPORT")
@SequenceGenerator(
		name="ERROR_SEQ_GEN",
		sequenceName="ERROR_SEQ",
		initialValue=1,
		allocationSize=1)
public class ErrorReport {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,
	generator="ERROR_SEQ_GEN")
	@Column(name="ERROR_ID")
	private long errorID;
	
	@Column(name="PROB_ID")
	private long probID;
	
	@Column(name="USER_UUID", length=36)
	private String userUUID;
	
	@Column(name="REPORT_TYPE", length=32)
	private String reportType;
	
	@Column(name="REPORT_TEXT", length=1024)
	private String reportText;
	
	@ManyToOne
	@JoinColumn(name="PROB_ID", nullable=false, insertable = false, updatable = false)
	private Problem problem;
	
	@ManyToOne
	@JoinColumn(name="USER_UUID", nullable=false, insertable = false, updatable = false)
	private UserMaster user;

}
