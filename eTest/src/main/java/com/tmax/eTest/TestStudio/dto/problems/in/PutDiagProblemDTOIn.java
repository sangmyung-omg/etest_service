package com.tmax.eTest.TestStudio.dto.problems.in;

import java.util.List;

import com.tmax.eTest.TestStudio.dto.problems.base.BaseDiagProblemSetDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutDiagProblemDTOIn {
	//Long
	private String userID;
	//
	private List<BaseDiagProblemSetDTO> diagProblems;
}
