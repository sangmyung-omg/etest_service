package com.tmax.eTest.Report.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import com.tmax.eTest.Contents.model.Problem;
import com.tmax.eTest.Contents.model.ProblemChoice;

@Component
public class ScoreCalculator {
	
	final static String RISK_SCORE_KEY = "리스크점수";
	final static String INVEST_COND_SCORE_KEY = "투자현황점수";
	final static String RISK_FIDELITY_SCORE_KEY = "위험적합도점수";
	final static String INVEST_RULE_SCORE_KEY = "투자원칙점수";
	final static String COGNITIVE_BIAS_SCORE_KEY = "인지편향점수";
	final static String DECISION_MAKING_SCORE_KEY = "의사결정적합도점수";
	

	// result = [투자현황 합계, 리스크 점수 합계, 위험 적합도 점수]
	public Map<String, Integer> calculateRiskFidelityScore(List<Problem> investConditionProbList, 
			List<Pair<Integer, Integer>> investCondAnswerList,
			List<Problem> riskProbList,
			List<Pair<Integer, Integer>> riskAnswerList)
	{
		final int INVEST_CRITERIA = 10;
		final int RISK_CRITERIA = 12;
		final int ADDED_SCORE_LIST[] = {9, 3, 7, 8};
		
		Map<String, Integer> res = new HashMap<>();
		int investConditionScore = makeScore(investConditionProbList, investCondAnswerList);
		int riskScore = makeScore(riskProbList, riskAnswerList);
		int addedScore = 0;
		
		if(investConditionScore >= INVEST_CRITERIA && riskScore >= RISK_CRITERIA)
			addedScore = ADDED_SCORE_LIST[0];
		else if(investConditionScore >= INVEST_CRITERIA && riskScore < RISK_CRITERIA)
			addedScore = ADDED_SCORE_LIST[1];
		else if(investConditionScore < INVEST_CRITERIA && riskScore >= RISK_CRITERIA)
			addedScore = ADDED_SCORE_LIST[2];
		else
			addedScore = ADDED_SCORE_LIST[3];
		
		int riskFidelityScore = Math.abs(investConditionScore-riskScore);
		riskFidelityScore = (605 - 25 * riskFidelityScore) / 7 + addedScore;
		
		res.put(RISK_SCORE_KEY, investConditionScore);
		res.put(INVEST_COND_SCORE_KEY, riskScore);
		res.put(RISK_FIDELITY_SCORE_KEY, riskFidelityScore);
		
		return res;
	}
	
	// result = [투자현황 합계, 리스크 점수 합계, 위험 적합도 점수]
	public Map<String, Integer> calculateDecisionMakingScore(List<Problem> investRuleProbList, 
			List<Pair<Integer, Integer>> investRuleAnswerList,
			List<Problem> cognitiveBiasProbList,
			List<Pair<Integer, Integer>> cognitiveBiasAnswerList)
	{
		final int INVEST_RULE_CRITERIA = 26;
		final int COGNITIVE_BIAS_CRITERIA = 35;
		final int ADDED_SCORE_LIST[] = {10,8,4,2};
		
		Map<String, Integer> res = new HashMap<>();
		int investRuleScore = makeScore(investRuleProbList, investRuleAnswerList);
		int cognitiveBiasScore = makeScore(cognitiveBiasProbList, cognitiveBiasAnswerList);
		int addedScore = 0;
		
		if(investRuleScore >= INVEST_RULE_CRITERIA && cognitiveBiasScore >= COGNITIVE_BIAS_CRITERIA)
			addedScore = ADDED_SCORE_LIST[0];
		else if(investRuleScore >= INVEST_RULE_CRITERIA && cognitiveBiasScore < COGNITIVE_BIAS_CRITERIA)
			addedScore = ADDED_SCORE_LIST[1];
		else if(investRuleScore < INVEST_RULE_CRITERIA && cognitiveBiasScore >= COGNITIVE_BIAS_CRITERIA)
			addedScore = ADDED_SCORE_LIST[2];
		else
			addedScore = ADDED_SCORE_LIST[3];
		
		int decisionMakingScore = investRuleScore+cognitiveBiasScore + addedScore;
		
		res.put(INVEST_RULE_SCORE_KEY, investRuleScore);
		res.put(COGNITIVE_BIAS_SCORE_KEY, cognitiveBiasScore);
		res.put(DECISION_MAKING_SCORE_KEY, decisionMakingScore);
		
		return res;
	}
	

	
	private int makeScore(List<Problem> probList, 
			List<Pair<Integer, Integer>> answerList)
	{
		int res = 0;
		
		Map<Integer, List<Integer>> probScores = new HashMap<>();
		for(Problem prob : probList)
		{
			List<ProblemChoice> probSelectList = prob.getProblemChoices();
			List<Integer> selectScoreList = new ArrayList<>(probSelectList.size());
			
			for(ProblemChoice choice : probSelectList)
			{
				// 추후 수정 필요.
				selectScoreList.add(Long.valueOf(choice.getChoiceNum()).intValue(), 
						//choice.getChoiceScore());
						Long.valueOf(choice.getChoiceNum()).intValue());
			}
			probScores.put(prob.getProbID(), selectScoreList);
		}
		
		for(Pair<Integer, Integer> investCondProbAnswer : answerList)
		{
			List<Integer> scoreList = probScores.get(investCondProbAnswer.getFirst());
			
			res += scoreList.get(investCondProbAnswer.getSecond());
		}
		
		return res;
	}
	
}
