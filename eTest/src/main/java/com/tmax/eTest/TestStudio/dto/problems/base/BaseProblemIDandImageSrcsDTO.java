package com.tmax.eTest.TestStudio.dto.problems.base;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseProblemIDandImageSrcsDTO {
	//Long
	private String probID;
	//
	private List<String> imgSrcs;
	
}
