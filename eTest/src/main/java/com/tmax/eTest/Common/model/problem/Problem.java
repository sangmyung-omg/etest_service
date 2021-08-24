package com.tmax.eTest.Common.model.problem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.tmax.eTest.Common.model.error_report.ErrorReport;

import com.tmax.eTest.Common.model.uk.ProblemUKRelation;
import com.tmax.eTest.Common.model.uk.UkMaster;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Data
@Getter
@Setter
@Entity
@Table(name="PROBLEM")
public class Problem {
	@Id
	@Column(name="PROB_ID")
	private Integer probID;
	
	@Column(name="ANSWER_TYPE", length=50)
	private String answerType;
	
	@Column(name="QUESTION", length=4000)
	private String question;
	
	@Column(name="SOLUTION", length=4000)
	private String solution;
	
	@Column(name="DIFFICULTY", length=20)
	private String difficulty;
	
	@Column(name="CATEGORY", length=20)
	private String category;
	
	@Column(name="IMG_SRC", length=256)
	private String imgSrc;
	
	@Column(name="TIME_RECOMMENDATION")
	private Long timeReco;
	
	@Column(name="CREATOR_ID", length=32)
	private String creatorId;
	
	@Column(name="CREATE_DATE")
	private Date createDate;
	
	@Column(name="VALIDATOR_ID", length=32)
	private String valiatorID;
	
	@Column(name="VALIDATE_DATE")
	private Date valiateDate;
	
	@Column(name="EDITOR_ID")
	private String editorID;
	
	@Column(name="EDIT_DATE")
	private Date editDate;
	
	@Column(name="SOURCE", length=100)
	private String source;
	
	@Column(name="INTENTION", length=100)
	private String intention;
	
	@Column(name="QUESTION_INITIAL", length=4000)
	private String questionInitial;
	
	@Column(name="SOLUTION_INITIAL", length=4000)
	private String solutionInitial;
	
	@OneToMany(mappedBy="probID")
	private List<ProblemUKRelation> problemUKReleations = new ArrayList<ProblemUKRelation>();
	
	@OneToMany(mappedBy="probID")
	private List<ProblemChoice> problemChoices = new ArrayList<ProblemChoice>();

	@OneToOne(mappedBy = "problem", fetch = FetchType.LAZY)
	private DiagnosisProblem diagnosisInfo;

	@OneToOne(mappedBy = "problem", fetch = FetchType.LAZY)
	private TestProblem testInfo;
	
	@OneToMany(mappedBy="problem")
	private List<ErrorReport> errors = new ArrayList<ErrorReport>();
}
