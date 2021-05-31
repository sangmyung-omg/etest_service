package com.tmax.eTest.Contents.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.tmax.eTest.dao.UkDAO;

import lombok.Data;

@Data
@Entity
@IdClass(ProbUKCompositeKey.class)
@Table(name="PROBLEM_UK_REL")
public class ProblemUKRelDAO{
	@Id
	@ManyToOne
	@JoinColumn(name="PROB_ID")
	private ProblemDAO probID;
	
	@Id
	@ManyToOne
	@JoinColumn(name="UK_UUID")
	private UkDAO ukUuid;
	
	
//	@Id
//	@Column(name="PROB_ID")
//	private long probID;
//	
//	@Id
//	@Column(name="UK_ID")
//	private String ukID;
	
//	@ManyToOne
//	@JoinColumn(name="PROB_ID")
//	private ProblemDAO problem;
//	
//	@ManyToOne
//	@JoinColumn(name="UK_ID")
//	private UKMasterDAO uk;
//	
	
}
