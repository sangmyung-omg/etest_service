package com.tmax.eTest.Common.model.problem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="DIAGNOSIS_CURRICULUM")
public class DiagnosisCurriculum {
	
	@Id
	@Column(name="CURRICULUM_ID")
	private Integer curriculumId;
	
	@Column(name="CHAPTER")
	private String chapter;

    @Column(name="SECTION")
	private String section;
	
    @Column(name="SUB_SECTION")
	private String subSection;
    
    @Column(name="SUBJECT")
	private String subject;
    
    @Column(name="STATUS")
	private String status;

	public void updateCurriculum(String status) {
		this.status = status;
	}
    
}
