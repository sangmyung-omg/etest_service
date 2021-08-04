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
@Table(name="TEST_PROBLEM")
public class TestProblem {
	@Id
	@Column(name="PROB_ID")
	private Integer probID;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PART_ID", nullable=true, insertable = false, updatable = false)
	private Part part;
	
	private String subject;
	
	private String status;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="PROB_ID", nullable=true, insertable = false, updatable = false)
	private Problem problem;

}