package com.tmax.eTest.Common.model.problem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.tmax.eTest.Common.model.uk.UkMaster;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
//@Data
@Getter
@Setter
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
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="UK_ID")
	private UkMaster ukId;
	
	@Column(name="CHOICE_SCORE")
	private Integer choiceScore;
	
	@Column(name="PROB_ID" , insertable = false, updatable = false)
	private Integer probIDOnly;
	
	@Column(name="UK_ID" , insertable = false, updatable = false)
	private Integer ukIDOnly;
	
}
