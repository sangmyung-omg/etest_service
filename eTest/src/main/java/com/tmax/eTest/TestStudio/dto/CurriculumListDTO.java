package com.tmax.eTest.TestStudio.dto;

import com.tmax.eTest.Common.model.problem.DiagnosisCurriculum;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurriculumListDTO {
	private Integer id;
	private String chapter;
	private String section;
	private String subSection;
	private String subject;
	private String status;
	
	public CurriculumListDTO(DiagnosisCurriculum c) {
		this.id = c.getCurriculumId();
		this.chapter = c.getChapter();
		this.section = c.getSection();
		this.subSection = c.getSubSection();
		this.subject = c.getSubject();
		this.status = c.getStatus();
	}
}