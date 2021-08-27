package com.tmax.eTest.TestStudio.dto.problems.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseProbChoiceDTO {
	// Long
	private String probID;
	private String choiceNum;
	private String ukID;
	private String choiceScore;
	//
}
