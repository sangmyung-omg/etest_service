package com.tmax.eTest.Contents.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.tmax.eTest.Test.model.UkMaster;

import lombok.Data;
@Data
@IdClass(ProblemChoiceCompositeKey.class)
@Entity
@Table(name="PROBLEM_CHOICE")
public class ProblemChoice {
	@Id
	@ManyToOne
	@JoinColumn(name="PROB_ID")
	private Problem probID;
	
	@Id
	@Column(name="CHOICE_NUM")
	private long choiceNum;
	
	@Column(name="TEXT", length=256)
	private String text;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="UK_UUID")
	private UkMaster ukUuid;
	
}
