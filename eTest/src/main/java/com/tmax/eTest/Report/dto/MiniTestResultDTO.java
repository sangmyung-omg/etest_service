package com.tmax.eTest.Report.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class MiniTestResultDTO {
//	@ApiModelProperty(
//			name = "actionType",
//			dataType = "String",
//			example = "submit",
//			value = "해당 활동의 동사.",
//			required = true)
	
	int score = 0;
	int percentage = 0;
	
	Map<String, String> partUnderstanding = new HashMap<String, String>();
	
	List<List<String>> weakPartDetail = new ArrayList<List<String>>();
	
	int[] diagnosisQuestionInfo = {0,0,0};
	

	public boolean initForDummy() {
	
		score = 80;
		percentage = 80;
		
		partUnderstanding.put("Dummy Part 1", "C");
		partUnderstanding.put("Dummy Part 2", "E");
		partUnderstanding.put("Dummy Part 3", "D");
		partUnderstanding.put("Dummy Part 4", "D");
		partUnderstanding.put("Dummy Part 5", "F");
		
		for(int i = 0; i < 3; i++)
		{
			List<String> detail = new ArrayList<>();
			
			detail.add("Part 5 - Detail "+i);
			detail.add("F");
			detail.add("C");
			
			weakPartDetail.add(detail);
		}
		
		diagnosisQuestionInfo[0] = 7;
		diagnosisQuestionInfo[1] = 1;
		diagnosisQuestionInfo[2] = 2;
		
		return true;
	}
}
