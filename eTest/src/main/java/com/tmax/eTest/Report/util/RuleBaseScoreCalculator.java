package com.tmax.eTest.Report.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tmax.eTest.Common.model.problem.DiagnosisCurriculum;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.ProblemChoice;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
// Rule Base 점수, Triton 점수 관련 Method 집합 Class
public class RuleBaseScoreCalculator {
	

//	final public static String RISK_TRACING_TRADE_PERIOD = "증권사거래기간";
//	final public static String RISK_TRACING_STOCK_RATIO = "자산중주식비중";
//	final public static String RISK_TRACING_STOCK_NUM = "보유주식수";
//	final public static String RISK_TRACING_PORTFOLIO = "보유포트폴리오";
	final public static String RISK_SCORE = "리스크점수";
	final public static String RISK_TRACING_SCORE = "트레이싱점수";
	final public static String RISK_PROFILE_SCORE = "프로파일점수";
	final public static String RISK_PROFILE_CAPA = "리스크감내역량점수";
	final public static String RISK_PROFILE_LEVEL = "리스크감내수준점수";
	
	final public static String INVEST_SCORE = "의사결정적합도점수";
	final public static String INVEST_TRACING = "투자원칙점수";
	final public static String INVEST_PROFILE = "인지편향점수";
	
	final public static String KNOWLEDGE_SCORE = "지식이해도";
	final public static String KNOWLEDGE_COMMON = "주식상식";
	final public static String KNOWLEDGE_TYPE = "종목고르기";
	final public static String KNOWLEDGE_CHANGE = "가격변동특징";
	final public static String KNOWLEDGE_SELL = "매매방법";
	
	final public static String GI_SCORE_KEY = "GI점수";
	final public static String RISK_ANSWER_1_KEY = "RiskA1";
	final public static String RISK_ANSWER_2_KEY = "RiskA2";
	final public static String STOCK_PERIOD_ANS = "증권사 거래기간";
	final public static String STOCK_RATIO_ANS = "주식비중답변";
	final public static String STOCK_NUM_ANS = "주식종목수답변";


	public Map<String,Integer> probDivideAndCalculateScoresV2(List<Pair<Problem, Integer>> probInfos)
	{
		Map<String,Integer> res = new HashMap<>();

		// 투자 의사 결정 적합도
		List<Pair<Problem, Integer>> investTracingProb = new ArrayList<>(); // 투자원칙
		List<Pair<Problem, Integer>> investProfileProb = new ArrayList<>(); // 인지편향
		
		// 리스크
		List<Pair<Problem, Integer>> riskTracingProb = new ArrayList<>(); // 투자현황
		List<Pair<Problem, Integer>> riskLevelProb = new ArrayList<>(); // 리스크 감내 수준
		List<Pair<Problem, Integer>> riskPatiProb = new ArrayList<>();	//리스크 감내 역량
		
		// 투자 지식
		List<Pair<Problem, Integer>> knowledgeCommonProb = new ArrayList<>(); // 주식상식
		List<Pair<Problem, Integer>> knowledgeTypeProb = new ArrayList<>(); // 종목고르기
		List<Pair<Problem, Integer>> knowledgeChangeProb = new ArrayList<>(); // 가격변동특징
		List<Pair<Problem, Integer>> knowledgeSellProb = new ArrayList<>(); // 매매방법

		for(Pair<Problem, Integer> probInfo : probInfos)
		{
			Problem prob = probInfo.getFirst();
			
			if(prob.getDiagnosisInfo() != null && prob.getDiagnosisInfo().getCurriculum() != null)
			{
				DiagnosisCurriculum curriculum = prob.getDiagnosisInfo().getCurriculum();
				String section = curriculum.getSection();
				
				switch(section)
				{
				case "투자현황":
					riskTracingProb.add(probInfo);
					if(probInfo.getFirst().getDiagnosisInfo().getCurriculumId() == 1)
						res.put(STOCK_PERIOD_ANS, probInfo.getSecond());
					else if(probInfo.getFirst().getDiagnosisInfo().getCurriculumId() == 2) // 주식 투자 비중
						res.put(STOCK_RATIO_ANS, probInfo.getSecond());
					else if(probInfo.getFirst().getDiagnosisInfo().getCurriculumId() == 3) // 보유 종목 수
						res.put(STOCK_NUM_ANS, probInfo.getSecond());
					break;
				case "리스크":
					if(curriculum.getSubSection().equals("리스크 감내역량"))
						riskPatiProb.add(probInfo);
					else
						riskLevelProb.add(probInfo);
					break;
				case "투자원칙":
					investTracingProb.add(probInfo);
					break;
				case "인지편향":
					investProfileProb.add(probInfo);
					break;
				case "투자지식":
					switch(curriculum.getSubSection())
					{
					case "투자 상식":
						knowledgeCommonProb.add(probInfo);
						break;
					case "종목고르기":
						knowledgeTypeProb.add(probInfo);
						break;
					case "가격변동 특징":
						knowledgeChangeProb.add(probInfo);
						break;
					case "매매방법":
						knowledgeSellProb.add(probInfo);
						break;
					default:
						log.info("probDivideAndCalculateScores section invalid : " + curriculum.getSubSection());
						break;
					}
					break;
				default:
					log.info("probDivideAndCalculateScores section invalid : " + section);
					break;
				}
			}
			else
			{
				log.info("probDivideAndCalculateScores prob not have diagnosisInfo : " + prob.toString());
			}
		}
		
		if(riskPatiProb.size() != 2) // 2문항
		{
			log.info("probDivideAndCalculateScores riskPatienceProb size error : " + riskPatiProb.size());
			res.put(RISK_ANSWER_1_KEY, 1);
			res.put(RISK_ANSWER_2_KEY, 1);
		}
		else
		{
			int q1Idx = riskPatiProb.get(0).getFirst().getProbID() < riskPatiProb.get(1).getFirst().getProbID()?0:1;
			int q2Idx = q1Idx==0?1:0;
			
			res.put(RISK_ANSWER_1_KEY, riskPatiProb.get(q1Idx).getSecond());
			res.put(RISK_ANSWER_2_KEY, riskPatiProb.get(q2Idx).getSecond());
		}
		
		res.putAll(calculateRiskScore(riskTracingProb, riskPatiProb, riskLevelProb));
		res.putAll(calculateDecisionMakingScore(investTracingProb, investProfileProb));
		res.putAll(calculateInvestKnowledgeScoreV2(
				knowledgeCommonProb, 
				knowledgeTypeProb,
				knowledgeChangeProb,
				knowledgeSellProb));
		res.put(GI_SCORE_KEY, Double.valueOf(res.get(RISK_SCORE) * 0.3
				+ res.get(INVEST_SCORE) * 0.3
				+ res.get(KNOWLEDGE_SCORE)* 0.4).intValue());
	
		
		return res;
	}

	// result = [리스크점수, 투자현황점수, 위험적합도점수]
	public Map<String, Integer> calculateRiskScore(
			List<Pair<Problem, Integer>> riskTracingProbList, 
			List<Pair<Problem, Integer>> riskCapaProbList, 
			List<Pair<Problem, Integer>> riskLevelProbList) {
		Map<String, Integer> res = new HashMap<>();
		
		int riskTracingScore = makeScore(riskTracingProbList);
		int riskCapaScore = makeScore(riskCapaProbList);
		int riskLevelScore = makeScore(riskLevelProbList);
		int riskProfileScore = riskCapaScore + riskLevelScore;

		float tempScore = Math.abs((riskTracingScore - 4) * 100 / 12.f - (riskProfileScore - 5) * 100 / 15.f);
		int riskScore = Float.valueOf((100 - tempScore) * 0.6f).intValue() + 35;

		res.put(RISK_SCORE, riskScore);
		res.put(RISK_TRACING_SCORE, riskTracingScore);
		res.put(RISK_PROFILE_SCORE, riskProfileScore);
		res.put(RISK_PROFILE_CAPA, riskCapaScore);
		res.put(RISK_PROFILE_LEVEL, riskProfileScore);

		return res;
	}

	// result = [투자원칙점수, 인지편향점수, 의사결정적합도점수]
	public Map<String, Integer> calculateDecisionMakingScore(
			List<Pair<Problem,Integer>> investTracingList,
			List<Pair<Problem,Integer>> investProfileList) {
		final int ADDED_SCORE_LIST[] = { 15, 12, 10, 7, 5 };

		Map<String, Integer> res = new HashMap<>();
		int tracingScore = makeScore(investTracingList);
		int profileScore = makeScore(investProfileList);
		int addedScoreIdx = (tracingScore >= 40) ? 0 
				: (tracingScore >= 33) ? 1 
				: (tracingScore >= 26) ? 2 
				: (tracingScore >= 19) ? 3
				: 4;

		tracingScore += ADDED_SCORE_LIST[addedScoreIdx];

		res.put(INVEST_TRACING, tracingScore);
		res.put(INVEST_PROFILE, profileScore);
		res.put(INVEST_SCORE, tracingScore + profileScore);

		return res;
	}
	
	private int calculateKnowledgePartScore(List<Pair<Problem, Integer>> probList)
	{
		int result = 0;
		int scoreMap[] = 
			{8, 6, 4, // 정답
			5, 3, 2}; // 오답
		
		for(Pair<Problem, Integer> probInfo : probList)
		{
			Problem prob = probInfo.getFirst();
			int choice = probInfo.getSecond();
			
			JsonArray solution = JsonParser.parseString(prob.getSolution()).getAsJsonArray();
			
			for(int i = 0; i < solution.size(); i++)
			{
				JsonObject jo = solution.get(i).getAsJsonObject();
				
				if(jo.get("type") != null && jo.get("data") != null &&
					jo.get("type").getAsString().equals("MULTIPLE_CHOICE_CORRECT_ANSWER"))
				{
					int answer = jo.get("data").getAsInt();
					int idx = choice == answer ? 0 : 3;
					idx += prob.getDifficulty().equals("상") ? 0 
						: prob.getDifficulty().equals("중") ? 1 
						: 2;					// 하
					
					result += scoreMap[idx];
					
					break;
				}
			}
		}
		
		return result;
	}
	
	private Map<String, Integer> calculateInvestKnowledgeScoreV2(
			List<Pair<Problem, Integer>> common,
			List<Pair<Problem, Integer>> type,
			List<Pair<Problem, Integer>> change,
			List<Pair<Problem, Integer>> sell)
	{
		Map<String, Integer> res = new HashMap<>();
		
		int commonScore = calculateKnowledgePartScore(common);
		int typeScore = calculateKnowledgePartScore(type);
		int sellScore = calculateKnowledgePartScore(sell);
		int changeScore = calculateKnowledgePartScore(change);
		int knowledgeScore = commonScore + typeScore + sellScore + changeScore;
		
		res.put(KNOWLEDGE_SCORE, knowledgeScore);
		res.put(KNOWLEDGE_COMMON, commonScore);
		res.put(KNOWLEDGE_TYPE, typeScore);
		res.put(KNOWLEDGE_SELL, sellScore);
		res.put(KNOWLEDGE_CHANGE, changeScore);
		
		return res;
	}
	
	private int makeScore(List<Pair<Problem, Integer>> probList)
	{
		int res = 0;
		
		for(Pair<Problem, Integer> prob : probList)
		{
			List<ProblemChoice> choices = prob.getFirst().getProblemChoices();
			for(ProblemChoice choice: choices)
			{
				if(choice.getChoiceNum() == prob.getSecond() && choice.getChoiceScore() != null)
				{
					res += choice.getChoiceScore();
					break;
				}
			}
		}
		
		return res;
	}	
	
}
