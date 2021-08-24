package com.tmax.eTest.TestStudio.dto.problems.in;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteProblemDTOIn {
	private String userID;
	//Long
	private List<String> probIDs;
	//
}
