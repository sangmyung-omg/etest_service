package com.tmax.eTest.TestStudio.dto.problems.out;

import java.util.List;

import com.tmax.eTest.TestStudio.dto.problems.base.BaseDiagProblemSetDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetDiagProblemDTOOut {
	//Long
	private List<BaseDiagProblemSetDTO> diagProblems;

}
