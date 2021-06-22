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
	
	int giPoint = 0;
	int giPercentage = 0;
	
	Map<String, Integer> partDiagnosisResult = new HashMap<String, Integer>();
	List<String> diagnosisDescription = new ArrayList<>();
	
	public boolean initForDummy() {
		giPoint = 63;
		giPercentage = 20;
		
		partDiagnosisResult.put("리스크 적합도", 72);
		partDiagnosisResult.put("투자 의사결정", 65);
		partDiagnosisResult.put("투자 지식", 52);
		
		
		diagnosisDescription.add("리스크가 있더라도 높은 수익을 추구하며, 의사결정도 계획적이고 명확히 하는 경향이 있습니다.");
		diagnosisDescription.add("투자경험이나 공부를 한다면 좋은 시너지가 날 수 있습니다.");
		diagnosisDescription.add("더미 테스트 1");
		diagnosisDescription.add("더미 테스트 2");
		diagnosisDescription.add("더미 테스트 3");		
		
		return true;
	}

}
