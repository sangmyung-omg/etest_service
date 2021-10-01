package com.tmax.eTest.TestStudio.dto.problems.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseDiagCurriculumDTO {
	// Long
	private String curriculumID;
	//
	private String chapter;
	private String section;
	private String subSection;
	private String subject;
//	private String setType;    //21.10.01 정책상 제외
	private String status;

}
