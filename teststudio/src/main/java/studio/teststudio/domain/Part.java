package studio.teststudio.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PART")
@Getter
@Setter
public class Part {

	@Id
	@Column(name = "PART_ID")
	private long partId;
	
	@Column(name = "PART_NAME")	
	private String partName;
	
	@Column(name = "ORDER_NUM")	
	private int orderNum;
	
	@Column(name = "PROBLEM_COUNT")	
	private int problemCount;
	
	@OneToMany(mappedBy = "part", cascade = CascadeType.ALL)
	private List<TestProblem> testProblem = new ArrayList<>();
	
}