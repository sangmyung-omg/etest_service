package com.tmax.eTest.Contents.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="DIAGNOSIS_CURRICULUM")
public class DiagnosisCurriculum {
	
	@Id
	@Column(name="CURRICULUM_ID")
	private Integer curriculumId;
	
	private String chapter;
	private String section;
	private String subSection;
	
	
}
