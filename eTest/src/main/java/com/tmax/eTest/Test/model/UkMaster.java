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
}
