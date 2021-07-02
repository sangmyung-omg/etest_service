package com.tmax.eTest.Report.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class DiagnosisResultDTO {
//	@ApiModelProperty(
//			name = "actionType",
//			dataType = "String",
//			example = "submit",
//			value = "해당 활동의 동사.",
//			required = true)
	
	int giScore = 0;
	int giPercentage = 0;
	
	Map<String, Integer> partDiagnosisResult = new HashMap<String, Integer>();
	List<String> diagnosisDescription = new ArrayList<>();
	List<List<String>> strongPartInfo = new ArrayList<>();
	List<List<String>> weakPartInfo = new ArrayList<>();
	List<String> similarTypeInfo = new ArrayList<>();
	
	
	public boolean pushStrongPartInfo(String partName, String partDescription, int score)
	{
		List<String> partInfo = new ArrayList<>();
		
		partInfo.add(partName);
		partInfo.add(partDescription);
		partInfo.add(String.valueOf(score));
		
		return strongPartInfo.add(partInfo);
	}
	
	public boolean pushWeakPartInfo(String partName, String partDescription, int score)
	{
		List<String> partInfo = new ArrayList<>();
		
		partInfo.add(partName);
		partInfo.add(partDescription);
		partInfo.add(String.valueOf(score));
		
		return weakPartInfo.add(partInfo);
	}
	
	public boolean initForDummy() {
		giScore = 63;
		giPercentage = 20;
		
		partDiagnosisResult.put("리스크 적합도", 72);
		partDiagnosisResult.put("투자 의사결정", 65);
		partDiagnosisResult.put("투자 지식", 52);
		
		
		diagnosisDescription.add("리스크가 있더라도 높은 수익을 추구하며, 의사결정도 계획적이고 명확히 하는 경향이 있습니다.");
		diagnosisDescription.add("투자경험이나 공부를 한다면 좋은 시너지가 날 수 있습니다.");
		diagnosisDescription.add("더미 테스트 1");
		diagnosisDescription.add("더미 테스트 2");
		diagnosisDescription.add("더미 테스트 3");		
		
		pushStrongPartInfo("Part 1", "Part 1 영역에서 상대적으로 높은 이해도를 갖고 있습니다.", 75);
		pushStrongPartInfo("Part 3", "Part 3 영역에서 상대적으로 높은 이해도를 갖고 있습니다.", 75);
		pushWeakPartInfo("Part 2", "Part 2는 더 많은 이해가 필요해 보입니다. AI가 학습 컨텐츠를 추천해드립니다.", 25);
		pushWeakPartInfo("Part 4", "Part 4는 더 많은 이해가 필요해 보입니다. AI가 학습 컨텐츠를 추천해드립니다.", 25);
		
		similarTypeInfo.add("15%");
		similarTypeInfo.add("5종목");
		similarTypeInfo.add("40%");
		
		
		return true;
	}

}
