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
	private Integer probId;
	
	@Column(name="SUBJECT")
	private String subject;

	@Column(name="SET_TYPE")
	private String setType;

	@Column(name="ORDER_NUM")
	private String orderNum;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CURRICULUM_ID", nullable=true, insertable = false, updatable = false)
	private DiagnosisCurriculum curriculum;
	
}
