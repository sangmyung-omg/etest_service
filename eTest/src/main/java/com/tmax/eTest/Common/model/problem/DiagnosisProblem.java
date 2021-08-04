package com.tmax.eTest.Common.model.problem;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="DIAGNOSIS_PROBLEM")
public class DiagnosisProblem {
	
	@Id
	@Column(name="PROB_ID")
	private Integer probId;

	@Column(name="ORDER_NUM")
	private Integer orderNum;

	@Column(name="CURRICULUM_ID")
	private Integer curriculumId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CURRICULUM_ID", nullable=true, insertable = false, updatable = false)
	private DiagnosisCurriculum curriculum;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="PROB_ID", nullable=true, insertable = false, updatable = false)
	private Problem problem;
	
}