package com.tmax.eTest.Common.model.report;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tmax.eTest.Common.model.user.UserMaster;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="MINITEST_REPORT")
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
	private String minitestUkMastery;
	
	@ManyToOne
	@JoinColumn(name="userUuid", insertable = false, updatable = false)
	@JsonManagedReference
	private UserMaster user;
}
