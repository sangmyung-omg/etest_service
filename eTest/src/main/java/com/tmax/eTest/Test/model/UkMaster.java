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

import com.tmax.eTest.Contents.dao.ProblemChoiceDAO;
import com.tmax.eTest.Contents.dao.ProblemUKRelDAO;

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
	private List<ProblemChoiceDAO> problemChoices = new ArrayList<ProblemChoiceDAO>();
	
	@OneToMany(mappedBy="ukUuid")
	private List<ProblemUKRelDAO> problemUkRels = new ArrayList<ProblemUKRelDAO>();

}
