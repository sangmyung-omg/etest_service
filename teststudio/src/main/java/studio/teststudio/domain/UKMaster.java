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
@Table(name = "UK_MASTER")
@Getter
@Setter
public class UKMaster {
	
	@Id
	@Column(name = "UK_ID")
	private Long ukId;
	@Column(name = "UK_NAME")
	private String ukName;
	@Column(name = "UK_DESCRIPTION")
	private String ukDescription;
	@Column(name = "TRAIN_UNSEEN")
	private String trainUnseen; 
	
	@OneToMany(mappedBy = "ukMaster", cascade = CascadeType.ALL)
	private List<ProblemUkRel> problemUkRel = new ArrayList<>();
	
	@OneToMany(mappedBy = "ukMaster", cascade = CascadeType.ALL)
	private List<ProblemChoice> problemChoice = new ArrayList<>();
		
}