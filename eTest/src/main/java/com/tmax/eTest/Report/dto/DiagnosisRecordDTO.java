package com.tmax.eTest.Report.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tmax.eTest.Common.model.problem.Problem;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class DiagnosisRecordDTO {
	
	// 최종 점수
	int giScore, giPercentage;

	// 점수 관련
	Map<String, Object> scoreInfo = new HashMap<>();
	
	// Comment 관련
	Map<String, String> mainCommentInfo = new HashMap<>();
	Map<String, Object> detailCommentInfo = new HashMap<>();
	
	// 문제 정보
	Map<String, Object> problemCorrectInfo = new HashMap<>();
	List<List<String>> problemHighLevelInfo = new ArrayList<>();
	List<List<String>> problemMiddleLevelInfo = new ArrayList<>();
	List<List<String>> problemLowLevelInfo = new ArrayList<>();
	
	private List<String> convertProblemToProbDTOInfo(Problem prob, int isCorr)
	{
		List<String> probInfoDTO = new ArrayList<>();
		
		probInfoDTO.add(prob.getProbID().toString());
		//probInfoDTO.add(isCorr);
		probInfoDTO.add("문제 제목");
		probInfoDTO.add("문제 내용");
		probInfoDTO.add("난이도");
		probInfoDTO.add("정답률");
		probInfoDTO.add("출처");
		
		return probInfoDTO;
	}
	
	// 문제 정보 삽입 함수.
	public boolean pushProblemList(List<Problem> probs, Map<Integer, Integer> probCorrInfo)
	{
		//맞은 문제 난이도 상 중 하, 전체 문제 난이도 상 중 하
		
		int[] probCorrAndAllNum = {0,0,0,0,0,0};
		for(Problem prob : probs)
		{
			int isCorr = probCorrInfo.get(prob.getProbID());
			
			
			switch(prob.getDifficulty())
			{
			case "상":
				break;
			case "중":
				break;
			case "하":
				break;
			default:
				break;
			}
		}
		
		return true;
	}
	
	public boolean initForDummy() {
		giScore = 85;
		giPercentage = 22;
		
		Map<String, Integer> riskScoreInfo = new HashMap<>();
		Map<String, Integer> investScoreInfo = new HashMap<>();
		Map<String, Integer> knowledgeScoreInfo = new HashMap<>();
		
		riskScoreInfo.put("score", 71);
		riskScoreInfo.put("scoreUpper10", 89);
		riskScoreInfo.put("scoreAvg", 65);
		riskScoreInfo.put("percentage", 20);
		//riskScoreInfo.put("profile", 15);
		//riskScoreInfo.put("tracing", 9);
		//riskScoreInfo.put("tracingLevel", 4);
		//riskScoreInfo.put("tracingCapa", 5);
		
		investScoreInfo.put("score", 85);
		investScoreInfo.put("scoreUpper10", 83);
		investScoreInfo.put("scoreAvg", 78);
		investScoreInfo.put("percentage", 8);
		//investScoreInfo.put("profile", 40);
		//investScoreInfo.put("tracing", 45);
		
		knowledgeScoreInfo.put("score", 89);
		knowledgeScoreInfo.put("scoreUpper10", 75);
		knowledgeScoreInfo.put("scoreAvg", 78);
		knowledgeScoreInfo.put("percentage", 8);
//		knowledgeScoreInfo.put("common", 22);
//		knowledgeScoreInfo.put("type", 21);
//		knowledgeScoreInfo.put("change", 22);
//		knowledgeScoreInfo.put("sell", 24);
		
		scoreInfo.put("risk", riskScoreInfo);
		scoreInfo.put("invest", investScoreInfo);
		scoreInfo.put("knowledge", knowledgeScoreInfo);
		
		
		mainCommentInfo.put("risk", "전형적인 방어형 투자자");
		mainCommentInfo.put("invest", "투자원칙은 확고하지 않지만 이성적이며 행동편향은 보통인 투자자");
		mainCommentInfo.put("knowledge", "우수");
		
		Map<String, String> riskCommentInfo = new HashMap<>();
		Map<String, String> investCommentInfo = new HashMap<>();
		Map<String, String> knowledgeCommentInfo = new HashMap<>();
		
		riskCommentInfo.put("투자위험에 대한 자세", "리스크 회피형");
		riskCommentInfo.put("투자 방법", "분산 투자형");
		
		investCommentInfo.put("행동편향 정도", "이성적인 투자자");
		investCommentInfo.put("투자원칙 정도", "투자원칙이 확고하지 않은 투자자");
		
		knowledgeCommentInfo.put("투자기초", "우수");
		knowledgeCommentInfo.put("종목고르기", "양호");
		knowledgeCommentInfo.put("가격변동특징", "우수");
		knowledgeCommentInfo.put("매매방법", "우수");
		
		detailCommentInfo.put("risk", riskCommentInfo);
		detailCommentInfo.put("invest", investCommentInfo);
		detailCommentInfo.put("knowledge", knowledgeCommentInfo);
		
		problemCorrectInfo.put("allCorr", 9);
		problemCorrectInfo.put("allWrong", 3);
		problemCorrectInfo.put("high", "2/3");
		problemCorrectInfo.put("middle", "5/7");
		problemCorrectInfo.put("low", "2/2");

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
