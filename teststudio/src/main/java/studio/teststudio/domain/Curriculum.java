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
@Table(name = "DIAGNOSIS_CURRICULUM")
@Getter
@Setter
public class Curriculum {
	
	@Id
	@Column(name = "CURRICULUM_ID")
	private long curriculumId;
	private String chapter;
	private String section;
	@Column(name = "SUB_SECTION")
	private String subSection;
	
	@OneToMany(mappedBy = "curriculum", cascade = CascadeType.ALL)
	private List<DiagnosisProblem> diagnosisProblem = new ArrayList<>();
	
}