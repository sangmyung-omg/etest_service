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
	List<List<String>> strongPartInfo = new ArrayList<>();
	List<List<String>> weakPartInfo = new ArrayList<>();
	List<String> similarTypeInfo = new ArrayList<>();
	
	List<String> totalResult = new ArrayList<>();		// 종합결과
	List<String> riskFidelity = new ArrayList<>();		// 리스크 적합도
	List<String> decisionMaking = new ArrayList<>();	// 투자 의사 결정
	List<String> investKnowledge = new ArrayList<>();	// 투자 지식
	
	Map<String, Integer> scoreMap = new HashMap<String,Integer>();
	
	
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
		
		totalResult.add("리스크가 있더라도 높은 수익을 추구하며, 의사 결정도 계획적이고 명확히 하는 경향이 있습니다.");
		riskFidelity.add("리스크 적합도는 주식 투자 시, 리스크에 대한 개인의 수용정도와 실제 "
				+ "투자현황과 차이를 분석하여, 성향에 맞는 투자를 하고 있는지를 진단합니다.");
		decisionMaking.add("투자 시, 발생할 수 있는 인지적 편향에 치우치지 않는 경향이 있지만 확고한 투자원칙 수립은 조금은 미흡한 편입니다.");
		investKnowledge.add("주식 어플리케이션에서 매도/매수를 할 수 있는 기본적인 지식은 있지만, "
				+ "Valuation, 리스크 관리 개념 등 지식 측면에서 한 단계 발전이 필요합니다.");
		
		
		
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
