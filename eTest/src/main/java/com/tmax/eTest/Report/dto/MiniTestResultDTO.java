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
	
	List<List<String>> partUnderstanding =new ArrayList<List<String>>();
	
	//List<List<String>> weakPartDetail = new ArrayList<List<String>>();
	
	Map<String, List<List<String>>> partUkDetail = new HashMap<>();
	
	List<List<String>> diagnosisQuestionInfo;


	public boolean initForDummy() {
	
		score = 80;
		percentage = 80;
		
		for(int i = 0; i < 3; i++)
		{
			List<String> detail = new ArrayList<>();
			
			detail.add("Dummy Part "+i);
			detail.add("F");
			detail.add("10");
			
			partUnderstanding.add(detail);
		}

//		for(int i = 0; i < 3; i++)
//		{
//			List<String> detail = new ArrayList<>();
//			
//			detail.add("Part 5 - Detail "+i);
//			detail.add("F");
//			detail.add("C");
//			
//			weakPartDetail.add(detail);
//		}
		
		if(diagnosisQuestionInfo == null)
		{
			diagnosisQuestionInfo = new ArrayList<>();
			diagnosisQuestionInfo.add(new ArrayList<>());
			diagnosisQuestionInfo.add(new ArrayList<>());
			diagnosisQuestionInfo.add(new ArrayList<>());
		}
		
		diagnosisQuestionInfo.get(0).add("1");
		diagnosisQuestionInfo.get(0).add("2");
		diagnosisQuestionInfo.get(1).add("3");
		diagnosisQuestionInfo.get(1).add("4");
		diagnosisQuestionInfo.get(2).add("5");
		diagnosisQuestionInfo.get(2).add("6");
		
		return true;
	}
}
