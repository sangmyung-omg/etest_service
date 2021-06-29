package com.tmax.eTest.Contents.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
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
	
	@Column(name="SET_NUM")
	private Integer setNum;
	
	@Column(name="SEQUENCE")
	private Integer sequence;

	@ManyToOne
	@JoinColumn(name="PROB_ID", nullable=true, insertable = false, updatable = false)
	private Problem problem;
	

}
