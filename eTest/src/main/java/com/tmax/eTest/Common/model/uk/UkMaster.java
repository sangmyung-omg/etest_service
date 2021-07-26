package com.tmax.eTest.Common.model.uk;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.tmax.eTest.Common.model.problem.ProblemChoice;

import lombok.Data;

@Data
@Entity
@Table(name="UK_MASTER")
public class UkMaster {
	@Id
	private String ukId;
	
	private String ukName;
	private String ukDescription;
	private String trainUnseen;
	private String part;
	
	@OneToMany(mappedBy="ukId")
	private List<ProblemChoice> problemChoices = new ArrayList<ProblemChoice>();
	
	@OneToMany(mappedBy="ukId")
	private List<ProblemUKRelation> problemUkRels = new ArrayList<ProblemUKRelation>();

}
