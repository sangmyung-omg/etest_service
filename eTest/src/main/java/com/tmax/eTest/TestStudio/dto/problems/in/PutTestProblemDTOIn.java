package com.tmax.eTest.TestStudio.dto.problems.in;

import java.util.List;


import com.tmax.eTest.TestStudio.dto.problems.base.BaseTestProblemSetDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutTestProblemDTOIn {
	//Long
	private String userID;
	//
	private List<BaseTestProblemSetDTO> testproblems;
}
