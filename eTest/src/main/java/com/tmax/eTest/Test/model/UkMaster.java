package com.tmax.eTest.Test.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.tmax.eTest.Contents.model.ProblemChoice;
import com.tmax.eTest.Contents.model.ProblemUKRelation;

import lombok.Data;

@Data
@Entity
@Table(name="UK_MASTER")
public class UkMaster {
	@Id
	private String ukUuid;
	
	private String ukName;
	private String ukDescription;
	private String trainUnseen;
	private String curriculumId;
	
	@OneToOne(cascade=(CascadeType.ALL))
	@JoinColumn(name="curriculumId", insertable = false, updatable = false)
	private CurriculumMaster curriculumDao;

	
	@OneToMany(mappedBy="ukUuid")
	private List<ProblemChoice> problemChoices = new ArrayList<ProblemChoice>();
	
	@OneToMany(mappedBy="ukUuid")
	private List<ProblemUKRelation> problemUkRels = new ArrayList<ProblemUKRelation>();

}
