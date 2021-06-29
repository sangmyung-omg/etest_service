package com.tmax.eTest.Contents.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="DIAGNOSIS_PROBLEM")
public class DiagnosisProblem {
	
	@Id
	@Column(name="PROB_ID")
	private Integer probID;
	
	@Column(name="SUBJECT")
	private String subject;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PROB_ID", nullable=true, insertable = false, updatable = false)
	private Problem problem;
	
	
	
}
