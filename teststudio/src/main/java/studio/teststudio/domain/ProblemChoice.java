package studio.teststudio.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PROBLEM_CHOICE")
@Getter
@Setter
public class ProblemChoice {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROB_ID")
	private Problem problem;
	
	@Column(name = "CHOICE_NUM")
	private int choiceNum;
	
	private String text;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UK_ID")
	private UKMaster ukMaster;
	
	@Column(name = "CHOICE_SCORE")
	private int choiceScore;
	
	 public void setProblem(Problem problem) {
		 this.problem = problem;
		 problem.getProblemChoice().add(this);
	 }
	 
	 public void setUkMaster(UKMaster ukMaster) {
		 this.ukMaster = ukMaster;
		 ukMaster.getProblemChoice().add(this);
	 }
	
}