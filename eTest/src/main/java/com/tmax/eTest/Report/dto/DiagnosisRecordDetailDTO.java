package com.tmax.eTest.Report.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tmax.eTest.Common.model.problem.Problem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Log4j2
public class DiagnosisRecordDetailDTO {

	int score = 0;
	int percentage = 0;
	
	// Comment 관련
	Map<String, String> mainCommentInfo = new HashMap<>();
	List<Object> detailCommentInfo = new ArrayList<>();
	
	List<List<String>> scoreInfo = new ArrayList<>();

	public void initForDummy()
	{
		mainCommentInfo.put("main", "현명한 하이브리드형 투자자");
		mainCommentInfo.put("detail", "“하이브리드형 투자자”는 공격적일때는 공격적으로, "+
				"보수적일때는 보수적으로 상황에 맞게 투자방법을 결정하는 경향이 있습니다. "
				+ "대체로 내가 감당할 수 있는 범위 내에서 투자하는 것을 선호하며, "
				+ "나의 위험성향과 실제 투자 방식이 상당부분 일치하기 때문에 주변 사람들이 "
				+ "봤을때는 안정적으로 투자하는 사람으로 보이기도 합니다.");
		
		Map<String, Object> riskCommentInfo = new HashMap<>();
		Map<String, Object> investInfo = new HashMap<>();
		
		riskCommentInfo.put("name", "투자위험 태도");
		riskCommentInfo.put("main", "너무 큰 위험도 너무 작은 위험도 추구하지 않는, 중립형 투자자");
		riskCommentInfo.put("detail", "당신은 리스크 중립형으로 위험을 적당하게 수용하고 인내하는 투자자입니다.");
		riskCommentInfo.put("score", 40);
		
		investInfo.put("name", "투자 방법");
		investInfo.put("main", "집중할 때는 집중, 분산할 때는 분산! 중간형 투자자");
		investInfo.put("detail", "당신은 집중할때는 집중하고 분산할때는 분산하기도 하지만, "
				+ "남들과 비슷한 수준의 분산 투자를 하는 경향이 있습니다");
		investInfo.put("score", 42);
		
		detailCommentInfo.add(riskCommentInfo);
		detailCommentInfo.add(investInfo);
	}
	
	
}
