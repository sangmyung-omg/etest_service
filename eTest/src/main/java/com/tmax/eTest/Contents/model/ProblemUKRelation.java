package com.tmax.eTest.Contents.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.tmax.eTest.Test.model.UkMaster;

import lombok.Data;

@Data
@Entity
@IdClass(ProbUKCompositeKey.class)
@Table(name="PROBLEM_UK_REL")
public class ProblemUKRelation{
	@Id
	@ManyToOne
	@JoinColumn(name="PROB_ID")
	private Problem probID;
	
	@Id
	@ManyToOne
	@JoinColumn(name="UK_ID")
	private UkMaster ukId;
	
	
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
