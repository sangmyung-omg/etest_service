package com.tmax.eTest.TestStudio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurriculumListDTO {
	private Integer id;
	private String chapter;
	private String section;
	private String subSection;
	private String setType;
	private String subject;
	private String status;
}