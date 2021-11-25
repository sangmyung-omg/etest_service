package com.tmax.eTest.Report.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Report.util.diagnosis.CommentMapper;
import com.tmax.eTest.Report.util.diagnosis.CommentMapper.Type;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class DiagnosisRecordMainDTO {
	
	// 최종 점수
	int giScore=0, giPercentage=0;
	String giComment = "";
	String giFixedComment = "";
	String userName = "이름";
	boolean alarm = false;

	// 점수 관련
	Map<String, Object> scoreInfo = new HashMap<>();

	// 문제 정보
	Map<String, Object> problemCorrectInfo = new HashMap<>();
	List<List<String>> problemHighLevelInfo = new ArrayList<>();
	List<List<String>> problemMiddleLevelInfo = new ArrayList<>();
	List<List<String>> problemLowLevelInfo = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	public boolean pushInfoByReport(
			DiagnosisReport report, 
			Map<String, Integer> percentageInfo,
			Map<String, String> commentInfo,
			Map<String, Object> probInfos,
			boolean isAlarm,
			String nickName)
	{
		boolean result = true;
		
		giScore = report.getGiScore();
		giPercentage = percentageInfo.get("gi");
		
		Map<String, Object> riskScoreInfo = new HashMap<>();
		Map<String, Object> investScoreInfo = new HashMap<>();
		Map<String, Object> knowledgeScoreInfo = new HashMap<>();
		
		riskScoreInfo.put("score", report.getRiskScore());
		riskScoreInfo.put("percentage", percentageInfo.get("risk"));
		riskScoreInfo.put("comment", commentInfo.get("risk"));
		
		investScoreInfo.put("score", report.getInvestScore());
		investScoreInfo.put("percentage", percentageInfo.get("invest"));
		investScoreInfo.put("comment", commentInfo.get("invest"));
		
		knowledgeScoreInfo.put("score", report.getKnowledgeScore());
		knowledgeScoreInfo.put("percentage", percentageInfo.get("knowledge"));
		knowledgeScoreInfo.put("comment", commentInfo.get("knowledge"));
		
		scoreInfo.put("risk", riskScoreInfo);
		scoreInfo.put("invest", investScoreInfo);
		scoreInfo.put("knowledge", knowledgeScoreInfo);
		
		this.alarm = isAlarm;
		this.userName = nickName;
		
		problemCorrectInfo = (Map<String, Object>) probInfos.get("problemCorrectInfo");
		problemHighLevelInfo = (List<List<String>>) probInfos.get("problemHighLevelInfo");
		problemMiddleLevelInfo = (List<List<String>>) probInfos.get("problemMiddleLevelInfo");
		problemLowLevelInfo = (List<List<String>>) probInfos.get("problemLowLevelInfo");
		
		if(giScore >= 80)
			this.giComment = CommentMapper.getComment(Type.GI, 0);
		else
			this.giComment = CommentMapper.getComment(Type.GI, 1);
		
		this.giFixedComment = CommentMapper.getComment(Type.GI, 2);
		
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
		
		return true;
	}

}
