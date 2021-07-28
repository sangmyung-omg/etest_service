package studio.teststudio.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TEST_PROBLEM")
@Getter
@Setter
public class TestProblem extends Problem {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "part")
	private Part part;
	
	private String subject;

}