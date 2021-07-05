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
import com.tmax.eTest.Contents.model.Problem;
import com.tmax.eTest.Contents.model.ProblemChoice;

@Component
// Rule Base 점수, Triton 점수 관련 Method 집합 Class
public class RuleBaseScoreCalculator {

	final static String RISK_SCORE_KEY = "리스크점수";
	final static String INVEST_COND_SCORE_KEY = "투자현황점수";
	final static String RISK_FIDELITY_SCORE_KEY = "위험적합도점수";
	final static String INVEST_RULE_SCORE_KEY = "투자원칙점수";
	final static String COGNITIVE_BIAS_SCORE_KEY = "인지편향점수";
	final static String DECISION_MAKING_SCORE_KEY = "의사결정적합도점수";
	final static String[] SELF_DIAGNOSIS_TYPE = { "AFB", // 공격적 / 감정적 / 초보자
			"AFE", // 공격적 / 감정적 / 전문가
			"ALB", // 공격적 / 논리적 / 초보자
			"ALE", // 공격적 / 논리적 / 전문가
			"SFB", // 보수적 / 감정적 / 초보자
			"SFE", // 보수적 / 감정적 / 전문가
			"SLB", // 보수적 / 논리적 / 초보자
			"SLE" // 보수적 / 논리적 / 전문가
	};
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	public String makeSelfDiagType(int riskFidelityScore, int decisionMakingScore, int investKnowledgeScore) {
		int idx = 0;

		idx += riskFidelityScore < 65 ? 4 : 0;
		idx += decisionMakingScore >= 65 ? 2 : 0;
		idx += investKnowledgeScore >= 60 ? 1 : 0;

		return SELF_DIAGNOSIS_TYPE[idx];
	}
	
	public List<String> makeSimilarTypeInfo(int riskFidelityScore, int decisionMakingScore, int investKnowledgeScore) {
		List<String> res = new ArrayList<>();
		
		int totalScore = riskFidelityScore >= 75 ? 3 : riskFidelityScore >= 55 ? 2 : 1;
		totalScore += decisionMakingScore >= 75 ? 3 : decisionMakingScore >= 55 ? 2 : 1;
		totalScore += investKnowledgeScore >= 70 ? 3 : investKnowledgeScore >= 50 ? 2 : 1;
				
		String investerRatio = totalScore == 9 || totalScore == 3 ? "3.7%"
				: totalScore > 5 ? "33.33%" : "25.93%";
		String avgItemNum = riskFidelityScore >= 75 ? "8종목" : riskFidelityScore >= 55 ? "5종목" : "3종목";
		String investRatio = riskFidelityScore >= 75 ? "55%" : riskFidelityScore >= 55 ? "40%" : "10%";
		
		res.add(investerRatio);
		res.add(avgItemNum);
		res.add(investRatio);
		
		return res;
	}
	
	public Map<String,Integer> probDivideAndCalculateScores(List<Pair<Problem, Integer>> probInfos)
	{
		Map<String,Integer> res = new HashMap<>();
		List<Pair<Problem, Integer>> investCondProb = new ArrayList<>(); // 투자현황
		List<Pair<Problem, Integer>> riskProb = new ArrayList<>(); // 리스크
		List<Pair<Problem, Integer>> investRuleProb = new ArrayList<>(); // 투자원칙
		List<Pair<Problem, Integer>> cogBiasProb = new ArrayList<>(); // 인지편향
		List<Pair<Problem, Integer>> investKnowledgeProb = new ArrayList<>(); // 투자지식

		for(Pair<Problem, Integer> probInfo : probInfos)
		{
			Problem prob = probInfo.getFirst();
			
			if(prob.getDiagnosisInfo() != null && prob.getDiagnosisInfo().getCurriculum() != null)
			{
				String section = prob.getDiagnosisInfo().getCurriculum().getSection();
				
				switch(section)
				{
				case "투자현황":
					investCondProb.add(probInfo);
					break;
				case "리스크":
					riskProb.add(probInfo);
					break;
				case "투자원칙":
					investRuleProb.add(probInfo);
					break;
				case "인지편향":
					cogBiasProb.add(probInfo);
					break;
				case "투자지식":
					investKnowledgeProb.add(probInfo); 
					break;
				default:
					logger.info("probDivideAndCalculateScores section invalid : " + section);
					break;
				}
			}
			else
			{
				logger.info("probDivideAndCalculateScores prob not have diagnosisInfo : " + prob.toString());
			}
		}
		
		logger.info("probDivideAndCalculateScores prob num : "
				+ investCondProb.size() +" "
				+ riskProb.size() +" "
				+ investRuleProb.size() +" "
				+ cogBiasProb.size() +" "
				+ investKnowledgeProb.size() +" ");
		
		res.putAll(calculateRiskFidelityScore(investCondProb, riskProb));
		res.putAll(calculateDecisionMakingScore(investRuleProb, cogBiasProb));
		res.put("지식이해도", calculateInvestKnowledgeScore(investKnowledgeProb));
		res.put("GI점수", res.get(RISK_FIDELITY_SCORE_KEY) + res.get(DECISION_MAKING_SCORE_KEY) + res.get("지식이해도"));
		
		return res;
	}

	// result = [리스크점수, 투자현황점수, 위험적합도점수]
	public Map<String, Integer> calculateRiskFidelityScore(
			List<Pair<Problem, Integer>> investConditionProbList, 
			List<Pair<Problem, Integer>> riskProbList) {
		Map<String, Integer> res = new HashMap<>();
		int investConditionScore = makeScore(investConditionProbList);
		int riskScore = makeScore(riskProbList);

		float tempScore = Math.abs((investConditionScore - 4) * 100 / 12.f - (riskScore - 5) * 100 / 15.f);
		int riskFidelityScore = Float.valueOf((100 - tempScore) * 0.6f).intValue() + 35;

		res.put(RISK_SCORE_KEY, investConditionScore);
		res.put(INVEST_COND_SCORE_KEY, riskScore);
		res.put(RISK_FIDELITY_SCORE_KEY, riskFidelityScore);

		return res;
	}

	// result = [투자원칙점수, 인지편향점수, 의사결정적합도점수]
	public Map<String, Integer> calculateDecisionMakingScore(
			List<Pair<Problem,Integer>> investRuleProbList,
			List<Pair<Problem,Integer>> cognitiveBiasProbList) {
		final int INVEST_RULE_CRITERIA = 26;
		final int COGNITIVE_BIAS_CRITERIA = 35;
		final int ADDED_SCORE_LIST[] = { 10, 8, 4, 1 };

		Map<String, Integer> res = new HashMap<>();
		int investRuleScore = makeScore(investRuleProbList);
		int cognitiveBiasScore = makeScore(cognitiveBiasProbList);
		int addedScore = 0;

		if (investRuleScore >= INVEST_RULE_CRITERIA && cognitiveBiasScore >= COGNITIVE_BIAS_CRITERIA)
			addedScore = ADDED_SCORE_LIST[0];
		else if (investRuleScore >= INVEST_RULE_CRITERIA && cognitiveBiasScore < COGNITIVE_BIAS_CRITERIA)
			addedScore = ADDED_SCORE_LIST[1];
		else if (investRuleScore < INVEST_RULE_CRITERIA && cognitiveBiasScore >= COGNITIVE_BIAS_CRITERIA)
			addedScore = ADDED_SCORE_LIST[2];
		else
			addedScore = ADDED_SCORE_LIST[3];

		int decisionMakingScore = investRuleScore + cognitiveBiasScore + addedScore;

		res.put(INVEST_RULE_SCORE_KEY, investRuleScore);
		res.put(COGNITIVE_BIAS_SCORE_KEY, cognitiveBiasScore);
		res.put(DECISION_MAKING_SCORE_KEY, decisionMakingScore);

		return res;
	}

	private int calculateInvestKnowledgeScore(List<Pair<Problem, Integer>> investKnowledgeProbList)
	{
		int res = 0;
		int scoreMap[] = 
			{8, 6, 4, // 정답
			5, 3, 2}; // 오답
		
		for(Pair<Problem, Integer> probInfo : investKnowledgeProbList)
		{
			Problem prob = probInfo.getFirst();
			int choice = probInfo.getSecond();
			
			JsonArray solution = JsonParser.parseString(prob.getSolution()).getAsJsonArray();
			
			for(int i = 0; i < solution.size(); i++)
			{
				JsonObject jo = solution.get(i).getAsJsonObject();
				
				if(jo.get("type") != null && jo.get("data") != null &&
					jo.get("type").getAsString() == "MULTIPLE_CHOICE_CORRECT_ANSWER")
				{
					int answer = jo.get("data").getAsInt();
					int idx = choice == answer ? 0 : 3;
					idx += prob.getDifficulty() == "상" ? 0 
						: prob.getDifficulty() == "중" ? 1 
						: 2;					// 하
					
					res += scoreMap[idx];
					
					break;
				}
			}
		}
		
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
				if(choice.getChoiceNum() == prob.getSecond())
				{
					res += choice.getChoiceScore();
					break;
				}
			}
		}
		
		return res;
	}	
	
}
