package com.tmax.eTest.TestStudio.dto.problems.in;

import java.util.List;


import com.tmax.eTest.TestStudio.dto.problems.base.BaseTestProblemandProblemDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostTestProblemDTOIn {
	//Long
	private String userID;
	//
	private List<BaseTestProblemandProblemDTO> testProblems;
}
