package com.tmax.eTest.TestStudio.dto.problems.out;

import java.util.List;

import com.tmax.eTest.TestStudio.dto.problems.base.BaseTestProblemSetDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostTestProblemDTOOut {
	//Long
	private String resultMessasge;
	private List<String> probIDs;
}
