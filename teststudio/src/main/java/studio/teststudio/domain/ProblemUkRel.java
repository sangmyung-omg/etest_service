package studio.teststudio.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PROBLEM_UK_REL")
@Getter
@Setter
public class ProblemUkRel {

	@MapsId("probId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROB_ID")
	private Problem problem;

	@MapsId("ukId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UK_ID")
	private UKMaster ukMaster;

	public void setProblem(Problem problem) {
		this.problem = problem;
		problem.getProblemUkRel().add(this);
	}
	
	public void setUkMaster(UKMaster ukMaster) {
		this.ukMaster = ukMaster;
		ukMaster.getProblemUkRel().add(this);
	}
}