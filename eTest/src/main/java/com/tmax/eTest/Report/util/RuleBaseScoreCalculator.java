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

	public String makeSelfDiagType(int riskFidelityScore, int decisionMakingScore, int investKnowledgeScore) {
		int idx = 0;

		idx += riskFidelityScore < 65 ? 4 : 0;
		idx += decisionMakingScore >= 65 ? 2 : 0;
		idx += investKnowledgeScore >= 60 ? 1 : 0;

		return SELF_DIAGNOSIS_TYPE[idx];
	}

	// result = [리스크점수, 투자현황점수, 위험적합도점수]
	public Map<String, Integer> calculateRiskFidelityScore(List<Problem> investConditionProbList,
			List<Pair<Integer, Integer>> investCondAnswerList, List<Problem> riskProbList,
			List<Pair<Integer, Integer>> riskAnswerList) {
		Map<String, Integer> res = new HashMap<>();
		int investConditionScore = makeScore(investConditionProbList, investCondAnswerList);
		int riskScore = makeScore(riskProbList, riskAnswerList);

		float tempScore = Math.abs((investConditionScore - 4) * 100 / 12.f - (riskScore - 5) * 100 / 15.f);
		int riskFidelityScore = Float.valueOf((100 - tempScore) * 0.6f).intValue() + 35;

		res.put(RISK_SCORE_KEY, investConditionScore);
		res.put(INVEST_COND_SCORE_KEY, riskScore);
		res.put(RISK_FIDELITY_SCORE_KEY, riskFidelityScore);

		return res;
	}

	// result = [투자원칙점수, 인지편향점수, 의사결정적합도점수]
	public Map<String, Integer> calculateDecisionMakingScore(List<Problem> investRuleProbList,
			List<Pair<Integer, Integer>> investRuleAnswerList, List<Problem> cognitiveBiasProbList,
			List<Pair<Integer, Integer>> cognitiveBiasAnswerList) {
		final int INVEST_RULE_CRITERIA = 26;
		final int COGNITIVE_BIAS_CRITERIA = 35;
		final int ADDED_SCORE_LIST[] = { 10, 8, 4, 1 };

		Map<String, Integer> res = new HashMap<>();
		int investRuleScore = makeScore(investRuleProbList, investRuleAnswerList);
		int cognitiveBiasScore = makeScore(cognitiveBiasProbList, cognitiveBiasAnswerList);
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

	public int calculateInvestKnowledgeScore(List<Problem> investKnowledgeProbList,
			List<Pair<Integer, Integer>> investKnowledgeAnswerList) {
		return makeScore(investKnowledgeProbList, investKnowledgeAnswerList);
	}
	
	public String calculateUKScoreString(float ukScore)
	{
		if(ukScore >= 85.f)
			return "A";
		else if(ukScore >= 70.f)
			return "B";
		else if(ukScore >= 50.f)
			return "C";
		else if(ukScore >= 30.f)
			return "D";
		else if(ukScore >= 15.f)
			return "E";
		else
			return "F";
	}

	private int makeScore(List<Problem> probList, List<Pair<Integer, Integer>> answerList) {
		int res = 0;

		Map<Integer, List<Integer>> probScores = new HashMap<>();
		for (Problem prob : probList) {
			List<ProblemChoice> probSelectList = prob.getProblemChoices();
			List<Integer> selectScoreList = new ArrayList<>(probSelectList.size());

			for (ProblemChoice choice : probSelectList) {
				// 추후 수정 필요.
				selectScoreList.add(Long.valueOf(choice.getChoiceNum()).intValue(),
						// choice.getChoiceScore());
						Long.valueOf(choice.getChoiceNum()).intValue());
			}
			probScores.put(prob.getProbID(), selectScoreList);
		}

		for (Pair<Integer, Integer> investCondProbAnswer : answerList) {
			List<Integer> scoreList = probScores.get(investCondProbAnswer.getFirst());

			res += scoreList.get(investCondProbAnswer.getSecond());
		}

		return res;
	}

	
	
	
}
