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

@Component
// Rule Base 점수, Triton 점수 관련 Method 집합 Class
public class RuleBaseScoreCalculator {

	final public static String RISK_SCORE_KEY = "리스크점수";
	final public static String INVEST_COND_SCORE_KEY = "투자현황점수";
	final public static String RISK_FIDELITY_SCORE_KEY = "위험적합도점수";
	final public static String INVEST_RULE_SCORE_KEY = "투자원칙점수";
	final public static String COGNITIVE_BIAS_SCORE_KEY = "인지편향점수";
	final public static String DECISION_MAKING_SCORE_KEY = "의사결정적합도점수";
	final public static String INVEST_KNOWLEDGE_KEY = "지식이해도";
	final public static String GI_SCORE_KEY = "GI점수";
	final public static String RISK_ANSWER_1_KEY = "RiskA1";
	final public static String RISK_ANSWER_2_KEY = "RiskA2";
	final public static String STOCK_RATIO_ANS = "주식비중답변";
	final public static String STOCK_NUM_ANS = "주식종목수답변";
	

	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	public Map<String,Integer> probDivideAndCalculateScores(List<Pair<Problem, Integer>> probInfos)
	{
		Map<String,Integer> res = new HashMap<>();
		List<Pair<Problem, Integer>> investCondProb = new ArrayList<>(); // 투자현황
		List<Pair<Problem, Integer>> riskProb = new ArrayList<>(); // 리스크
		List<Pair<Problem, Integer>> investRuleProb = new ArrayList<>(); // 투자원칙
		List<Pair<Problem, Integer>> cogBiasProb = new ArrayList<>(); // 인지편향
		List<Pair<Problem, Integer>> investKnowledgeProb = new ArrayList<>(); // 투자지식
		List<Pair<Problem, Integer>> riskPatienceProb = new ArrayList<>();

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
					investCondProb.add(probInfo);
					if(probInfo.getFirst().getDiagnosisInfo().getCurriculumId() == 2) // 주식 투자 비중
						res.put(STOCK_RATIO_ANS, probInfo.getSecond());
					else if(probInfo.getFirst().getDiagnosisInfo().getCurriculumId() == 3) // 보유 종목 수
						res.put(STOCK_NUM_ANS, probInfo.getSecond());
					break;
				case "리스크":
					riskProb.add(probInfo);
					if(curriculum.getSubSection().equals("리스크 감내역량"))
						riskPatienceProb.add(probInfo);
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
		
		if(riskPatienceProb.size() != 2) // 2문항
		{
			logger.info("probDivideAndCalculateScores riskPatienceProb size error : " + riskPatienceProb.size());
			res.put(RISK_ANSWER_1_KEY, 1);
			res.put(RISK_ANSWER_2_KEY, 1);
		}
		else
		{
			int q1Idx = riskPatienceProb.get(0).getFirst().getProbID() < riskPatienceProb.get(1).getFirst().getProbID()?0:1;
			int q2Idx = q1Idx==0?1:0;
			
			res.put(RISK_ANSWER_1_KEY, riskPatienceProb.get(q1Idx).getSecond());
			res.put(RISK_ANSWER_2_KEY, riskPatienceProb.get(q2Idx).getSecond());
		}
		
		res.putAll(calculateRiskFidelityScore(investCondProb, riskProb));
		res.putAll(calculateDecisionMakingScore(investRuleProb, cogBiasProb));
		res.put(INVEST_KNOWLEDGE_KEY, calculateInvestKnowledgeScore(investKnowledgeProb));
		res.put(GI_SCORE_KEY, Double.valueOf(res.get(RISK_FIDELITY_SCORE_KEY) * 0.3
				+ res.get(DECISION_MAKING_SCORE_KEY) * 0.3
				+ res.get(INVEST_KNOWLEDGE_KEY)* 0.4).intValue());
		
		logger.info("probDivideAndCalculateScores prob num : "
				+ investCondProb.size() +" "
				+ riskProb.size() +" "
				+ investRuleProb.size() +" "
				+ cogBiasProb.size() +" "
				+ investKnowledgeProb.size() +" "
				+ res.get(RISK_FIDELITY_SCORE_KEY) + " "
				+ res.get(DECISION_MAKING_SCORE_KEY) + " "
				+ res.get(INVEST_KNOWLEDGE_KEY));
		
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

		res.put(RISK_SCORE_KEY, riskScore);
		res.put(INVEST_COND_SCORE_KEY, investConditionScore);
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
					jo.get("type").getAsString().equals("MULTIPLE_CHOICE_CORRECT_ANSWER"))
				{
					int answer = jo.get("data").getAsInt();
					int idx = choice == answer ? 0 : 3;
					idx += prob.getDifficulty().equals("상") ? 0 
						: prob.getDifficulty().equals("중") ? 1 
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
