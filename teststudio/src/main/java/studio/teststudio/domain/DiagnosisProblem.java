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
@Table(name = "DIAGNOSIS_PROBLEM")
@Getter
@Setter
public class DiagnosisProblem extends Problem {

	private String subject;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURRICULUM_ID")
	private Curriculum curriculum;

	private char setType;

	@Column(name = "ORDER_NUM")
	private Long orderNum;

	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
		curriculum.getDiagnosisProblem().add(this);
	}

}