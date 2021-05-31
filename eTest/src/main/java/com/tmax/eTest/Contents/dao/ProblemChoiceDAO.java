package com.tmax.eTest.Contents.dao;

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
@IdClass(ProblemChoiceCompositeKey.class)
@Entity
@Table(name="PROBLEM_CHOICE")
public class ProblemChoiceDAO {
	@Id
	@ManyToOne
	@JoinColumn(name="PROB_ID")
	private ProblemDAO probID;
	
	@Id
	@Column(name="CHOICE_NUM")
	private long choiceNum;
	
	@Column(name="TEXT", length=256)
	private String text;
	
	@ManyToOne
	@JoinColumn(name="UK_UUID")
	private UkDAO ukUuid;
	
}
