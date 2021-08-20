package com.tmax.eTest.Report.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class DiagnosisRecordDTO {
	
	// 최종 점수
	int giScore, giPercentage;

	// 점수 관련
	Map<String, Integer> riskScoreInfo;
	Map<String, Integer> investScoreInfo;
	Map<String, Integer> knowledgeScoreInfo;
	
	// Comment 관련
	Map<String, String> riskCommentInfo;
	Map<String, String> investCommentInfo;
	Map<String, String> knowledgeCommentInfo;
	
	// 문제 정보
	Map<String, Integer> problemCorrectInfo;
	List<List<String>> problemHighLevelInfo;
	List<List<String>> problemMiddleLevelInfo;
	List<List<String>> problemLowLevelInfo;
	
	public boolean initForDummy() {
		giScore = 85;
		giPercentage = 22;
		
		riskScoreInfo.put("score", 71);
		riskScoreInfo.put("scoreUpper10", 89);
		riskScoreInfo.put("profile", 15);
		riskScoreInfo.put("tracing", 9);
		riskScoreInfo.put("tracingLevel", 4);
		riskScoreInfo.put("tracingCapa", 5);
		
		riskCommentInfo.put("main", "전형적인 방어형 투자자");
		riskCommentInfo.put("투자위험에 대한 자세", "리스크 회피형");
		riskCommentInfo.put("투자 방법", "분산 투자형");
		
		investScoreInfo.put("score", 85);
		investScoreInfo.put("scoreUpper10", 83);
		investScoreInfo.put("profile", 40);
		investScoreInfo.put("tracing", 45);
		
		investCommentInfo.put("main", "투자원칙은 확고하지 않지만 이성적이며 행동편향은 보통인 투자자");
		investCommentInfo.put("행동편향 정도", "이성적인 투자자");
		investCommentInfo.put("투자원칙 정도", "투자원칙이 확고하지 않은 투자자");
		
		knowledgeScoreInfo.put("score", 89);
		knowledgeScoreInfo.put("scoreUpper10", 75);
		knowledgeScoreInfo.put("common", 22);
		knowledgeScoreInfo.put("type", 21);
		knowledgeScoreInfo.put("change", 22);
		knowledgeScoreInfo.put("sell", 24);
		
		knowledgeCommentInfo.put("main", "우수");
		knowledgeCommentInfo.put("투자기초", "우수");
		knowledgeCommentInfo.put("종목고르기", "양호");
		knowledgeCommentInfo.put("가격변동특징", "우수");
		knowledgeCommentInfo.put("매매방법", "우수");
		
		problemCorrectInfo.put("allCorr", 12);
		problemCorrectInfo.put("allWrong", 9);
		problemCorrectInfo.put("highCorr", 2);
		problemCorrectInfo.put("highWrong", 1);
		problemCorrectInfo.put("middleCorr", 5);
		problemCorrectInfo.put("middleWrong", 2);
		problemCorrectInfo.put("lowCorr", 2);
		problemCorrectInfo.put("lowWrong", 0);

		List<String> probInfo = new ArrayList<>();
		probInfo.add("문제 UUID");
		probInfo.add("정오답 여부(true or false)");
		probInfo.add("문제 제목");
		probInfo.add("문제 내용");
		probInfo.add("난이도");
		probInfo.add("정답률");
		probInfo.add("출처");
		
		problemHighLevelInfo.add(probInfo);
		problemHighLevelInfo.add(probInfo);
		problemHighLevelInfo.add(probInfo);
		problemMiddleLevelInfo.add(probInfo);
		problemMiddleLevelInfo.add(probInfo);
		problemMiddleLevelInfo.add(probInfo);
		problemMiddleLevelInfo.add(probInfo);
		problemMiddleLevelInfo.add(probInfo);
		problemMiddleLevelInfo.add(probInfo);
		problemMiddleLevelInfo.add(probInfo);
		problemMiddleLevelInfo.add(probInfo);
		problemLowLevelInfo.add(probInfo);
		problemLowLevelInfo.add(probInfo);
		
		return true;
	}

}
