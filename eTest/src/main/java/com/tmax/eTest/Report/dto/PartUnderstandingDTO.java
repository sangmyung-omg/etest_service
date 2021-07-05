package com.tmax.eTest.Report.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class PartUnderstandingDTO {
//	@ApiModelProperty(
//			name = "actionType",
//			dataType = "String",
//			example = "submit",
//			value = "해당 활동의 동사.",
//			required = true)
	
	int point = 0;
	
	Map<String, Integer> ukUnderstanding = new HashMap<String, Integer>();

	public boolean initForDummy() {
		ukUnderstanding.put("Dummy UK 1", 60);
		ukUnderstanding.put("Dummy UK 2", 70);
		ukUnderstanding.put("Dummy UK 3", 80);
	
		point = 70;
		return true;
	}
}
