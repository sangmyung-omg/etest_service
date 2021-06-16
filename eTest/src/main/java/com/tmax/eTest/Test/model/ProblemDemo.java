package com.tmax.eTest.Test.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="PROBLEM_DEMO")
public class ProblemDemo {
	@Id
	private String probUuid;
	
	private String ukId;
	
	private String chapter;
	private String difficulty;
	private String probTypeUuid;
	
	@OneToOne(cascade=(CascadeType.ALL))
	@JoinColumn(name="chapter", insertable = false, updatable = false)
	private CurriculumMaster curriculumDao;
	
	@OneToOne(cascade=(CascadeType.ALL))
	@JoinColumn(name="ukId", insertable = false, updatable = false)
	private UkMaster ukDao;
	
	@OneToOne(cascade=(CascadeType.ALL))
	@JoinColumn(name="probTypeUuid", insertable = false, updatable = false)
	private TypeUkMaster typeUkDao;
}
