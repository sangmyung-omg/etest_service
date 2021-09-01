package com.tmax.eTest.Report.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.report.DiagnosisReport;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class DiagnosisRecordMainDTO {
	
	// 최종 점수
	int giScore=0, giPercentage=0;
	String userName = "이름";

	// 점수 관련
	Map<String, Object> scoreInfo = new HashMap<>();
	
	List<String> similarTypeInfo = new ArrayList<>();
	
	
	public boolean pushInfoByReport(
			DiagnosisReport report, 
			Map<String, Integer> percentageInfo,
			List<String> similarTypeInfo)
	{
		boolean result = true;
		
		giScore = report.getGiScore();
		giPercentage = percentageInfo.get("gi");
		
		Map<String, Integer> riskScoreInfo = new HashMap<>();
		Map<String, Integer> investScoreInfo = new HashMap<>();
		Map<String, Integer> knowledgeScoreInfo = new HashMap<>();
		
		riskScoreInfo.put("score", report.getRiskScore());
		riskScoreInfo.put("percentage", percentageInfo.get("risk"));
		
		investScoreInfo.put("score", report.getInvestScore());
		investScoreInfo.put("percentage", percentageInfo.get("invest"));
		
		knowledgeScoreInfo.put("score", report.getKnowledgeScore());
		knowledgeScoreInfo.put("percentage", percentageInfo.get("knowledge"));
		
		scoreInfo.put("risk", riskScoreInfo);
		scoreInfo.put("invest", investScoreInfo);
		scoreInfo.put("knowledge", knowledgeScoreInfo);
		
		this.similarTypeInfo = similarTypeInfo;
		
		return result;
	}
	
	public boolean initForDummy() {
		giScore = 85;
		giPercentage = 22;
		
		userName = "홍길동";
		
		Map<String, Integer> riskScoreInfo = new HashMap<>();
		Map<String, Integer> investScoreInfo = new HashMap<>();
		Map<String, Integer> knowledgeScoreInfo = new HashMap<>();
		
		riskScoreInfo.put("score", 71);
		riskScoreInfo.put("percentage", 20);
		
		investScoreInfo.put("score", 85);
		investScoreInfo.put("percentage", 8);
		
		knowledgeScoreInfo.put("score", 89);
		knowledgeScoreInfo.put("percentage", 8);
		
		scoreInfo.put("risk", riskScoreInfo);
		scoreInfo.put("invest", investScoreInfo);
		scoreInfo.put("knowledge", knowledgeScoreInfo);
		
		similarTypeInfo.add("15%");
		similarTypeInfo.add("5종목");
		similarTypeInfo.add("40%");
		
		return true;
	}

}
