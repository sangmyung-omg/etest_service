package com.tmax.eTest.Report.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SelfDiagnosisComment {
	
	final public static String TOTAL_RES_KEY = "totalResult";
	final public static String RISK_FID_KEY = "riskFidelity";
	final public static String DECISION_MAKING_KEY = "decisionMaking";
	final public static String INVEST_KNOWLEDGE_KEY = "investKnowledge";
	final public static String SIMILAR_TYPE_KEY = "similarType";
	final public static String SELF_DIAG_TYPE_KEY = "selfDiagType";
	
	

	final static String[] SELF_DIAGNOSIS_TYPE = { "AFB", // 공격적 / 감정적 / 초보자
			"AFE", // 공격적 / 감정적 / 전문가
			"ALB", // 공격적 / 논리적 / 초보자
			"ALE", // 공격적 / 논리적 / 전문가
			"SFB", // 보수적 / 감정적 / 초보자
			"SFE", // 보수적 / 감정적 / 전문가
			"SLB", // 보수적 / 논리적 / 초보자
			"SLE" // 보수적 / 논리적 / 전문가
	};
	
	private String makeSelfDiagType(int riskFidelityScore, int decisionMakingScore, int investKnowledgeScore) {
		int idx = 0;

		idx += riskFidelityScore < 65 ? 4 : 0;
		idx += decisionMakingScore >= 65 ? 2 : 0;
		idx += investKnowledgeScore >= 60 ? 1 : 0;

		return SELF_DIAGNOSIS_TYPE[idx];
	}
	
	private List<String> makeSimilarTypeInfo(int riskFidelityScore, int decisionMakingScore, int investKnowledgeScore) {
		List<String> res = new ArrayList<>();
		
		int totalScore = riskFidelityScore >= 75 ? 3 : riskFidelityScore >= 55 ? 2 : 1;
		totalScore += decisionMakingScore >= 75 ? 3 : decisionMakingScore >= 55 ? 2 : 1;
		totalScore += investKnowledgeScore >= 70 ? 3 : investKnowledgeScore >= 50 ? 2 : 1;
				
		String investerRatio = totalScore == 9 || totalScore == 3 ? "3.7%"
				: totalScore > 5 ? "33.33%" : "25.93%";
		String avgItemNum = riskFidelityScore >= 75 ? "8종목" : riskFidelityScore >= 55 ? "5종목" : "3종목";
		String investRatio = riskFidelityScore >= 75 ? "55%" : riskFidelityScore >= 55 ? "40%" : "10%";
		String similarTypeStr = makeSelfDiagType(riskFidelityScore, decisionMakingScore, investKnowledgeScore);
		
		res.add(investerRatio);
		res.add(avgItemNum);
		res.add(investRatio);
		res.add(similarTypeStr);
		
		return res;
	}
	
	
	
	String[] finalResultComment ={
		"하하하",	"하하중",	"하하상",
		"하중하",	"하중중",	"하중상",
		"하상하",	"하상중",	"하상상",
		"중하하",	"중하중",	"중하상",
		"중중하",	"중중중",	"중중상",
		"중상하",	"중상중",	"중상상",
		"상하하",	"상하중",	"상하상",
		"상중하",	"상중중",	"상중상",
		"상상하",	"상상중",	"상상상",
	};
	
	// 모두 트리톤을 타지 않고 나오는 점수들. (단순 알고리즘으로 나오는 점수)
	// 위험 적합도 = riskScore
	// 투자 결정 적합도 = investDecisionScore
	// 지식 이해도 = knowledgeScore
	private List<String> getFinalResultComment(int riskFidelityScore, int decisionMakingScore, int knowledgeScore)
	{
		List<String> res = new ArrayList<>();
		int idx = 0;
		
		idx += riskFidelityScore >= 75 ? 18 : riskFidelityScore >= 55 ? 9 : 0;
		idx += decisionMakingScore >= 75 ? 6 : decisionMakingScore >= 55 ? 3 : 0;
		idx += knowledgeScore >= 70 ? 2 : knowledgeScore >= 50 ? 1 : 0;
		
		res.add(finalResultComment[idx]);
		
		List<String> silmilarType = makeSimilarTypeInfo(riskFidelityScore, decisionMakingScore, knowledgeScore);
		
		res.add("동일한 유형의 평균 주식 비중은 "+ silmilarType.get(0)+" 입니다.");
		res.add("동일한 유형의 평균 주식 수는 "+ silmilarType.get(1)+" 입니다.");
		res.add("동일한 유형의 평균 선호종목 수는 "+ silmilarType.get(2)+" 입니다.");
		
		return res;
	}
	
	String[] riskDiagnosisComment = {		// 리스트 적합도
		"투자현황 보수적 & 리스크 보수적",
		"투자현황 보수적 & 리스크 공격적",
		"투자현황 공격적 & 리스크 보수적",
		"투자현황 공격적 & 리스크 공격적"
	};
		
	String[] riskPatienceComment = {			// 리스크 감내 역량
		"안정적 투자를 지향하지만 투자 가능 기간이 긴 편은 아니기 때문에 꾸준한 모니터링이 필요함", // 보수, 보수, 답변 조건 X
		"리스크 감내역량이 있으니, 공격적 투자 고려 가능", // 보수, 보수, 답변 조건 O
		"공격적 투자 성향을 가지고 있지만, 투자자본 등 여건 상 보수적 투자방식을 유지하는게 안정적일 수 있음", // 보수, 공격, 답변 조건 X
		"리스크 감내하는 성향을 가지고 있고 역량도 갖춘 상태이므로, 공격적 투자 고려 가능", // 보수, 공격, O
		"성향과 역량을 고려하지 않는 투자를 하고 있으므로, 안정적 포트폴리오로 변경을 고려해야 함", // 공격, 보수, X
		"리스크 감당할 역량은 있기 때문에 현재의 공격적 투자 가능할 수 있음. 단 본인의 성향과 맞추어 투자하는 것을 고려해 볼 수 있음", // 공격, 보수, O
		"성향대로 공격적인 투자를 하고 있지만, 현실적으로 리스크를 감당할 수 있는 여건이 부족한 상황이므로, 안정적 포트폴리오로 구성 변경을 고려해볼 수 있음", // 공격, 공격, X
		"리스크 성향, 감내역량 충족"		
	};
	
	
	// 리스크 적합도 진단
	private List<String> getRiskFielityComment(int investCondScore, int riskScore, int riskQ1, int riskQ2)
	{
		List<String> res = new ArrayList<>();
		int idx = 0;
		
		idx += investCondScore >= 10 ? 2 : 0;
		idx += riskScore >= 12 ? 1 : 0;
		
		res.add(riskDiagnosisComment[idx]);
		
		idx = 0;
		idx += investCondScore >= 10 ? 4 : 0;
		idx += riskScore >= 12 ? 2 : 0;
		idx += (riskQ1 == 1 && riskQ2 != 1) ? 1 : 0;
		
		res.add(riskPatienceComment[idx]);
		
		return res;
	}
	
	
	String[] investDecisionComment = {
		"투자원칙 명확 & 편향 논리적",
		"투자원칙 명확 & 편향 감정적",
		"투자원칙 불명확 & 편향 논리적",
		"투자원칙 불명확 & 편향 감정적"
	};
	
	String investRuleIndefiniteComment = "투자원칙 불명확";
	
	String cogBiasEmotionalComment = "편향 감정적";
	
	private List<String> getDecisionMakingComment(int investRuleScore, int cognitiveBiasScore)
	{
		List<String> res = new ArrayList<>();
		
		int idx = 0;
		
		idx += investRuleScore >= 26 ? 2 : 0;
		idx += cognitiveBiasScore >= 35 ? 1 : 0;
		
		res.add(investDecisionComment[idx]);
		
		if(investRuleScore < 26)
			res.add(investRuleIndefiniteComment);
		
		if(cognitiveBiasScore < 35)
			res.add(cogBiasEmotionalComment);
		
		return res;
	}
	
	String[] investKnowledgeComment = {
		"지식수준 상",
		"지식수준 중",
		"지식수준 하",
	};
	
	String[] studyDirectionComment = {
		"취약 UK 관련 컨텐츠",
		"기본지식 + 취약 UK 관련 컨텐츠",
		"기본지식 및 소양 학습"
	};
	
	private List<String> getInvestKnowledgeComment(int investKnowledgeScore)
	{
		List<String> res = new ArrayList<>();
		
		int idx =  investKnowledgeScore >= 80 ? 0 : investKnowledgeScore >= 50 ? 1 : 2;
		
		res.add(investKnowledgeComment[idx]);
		res.add(studyDirectionComment[idx]);
		
		return res;
	}
	
	
	
	public Map<String, List<String>> getTotalComments(Map<String, Integer> scoreMap)
	{
		Map<String, List<String>> res = new HashMap<>();
		
		//(int riskFidelityScore, int investDecisionScore, int knowledgeScore)

		int riskFidelScore = scoreMap.get(RuleBaseScoreCalculator.RISK_PROFILE_SCORE);
		int decisionMakingScore = scoreMap.get(RuleBaseScoreCalculator.INVEST_SCORE);
		int riskA1 = scoreMap.get(RuleBaseScoreCalculator.RISK_ANSWER_1_KEY);
		int riskA2 = scoreMap.get(RuleBaseScoreCalculator.RISK_ANSWER_2_KEY);
		int riskScore = scoreMap.get(RuleBaseScoreCalculator.RISK_SCORE);
		int investCondScore = scoreMap.get(RuleBaseScoreCalculator.RISK_TRACING_SCORE);
		int investKnowledgeScore = scoreMap.get(RuleBaseScoreCalculator.KNOWLEDGE_SCORE);
		int investRuleScore = scoreMap.get(RuleBaseScoreCalculator.INVEST_TRACING);
		int cogBiasScore = scoreMap.get(RuleBaseScoreCalculator.INVEST_PROFILE);
		List<String> similarTypeList = makeSimilarTypeInfo(riskFidelScore, decisionMakingScore, investKnowledgeScore);
		
		res.put(TOTAL_RES_KEY, getFinalResultComment(riskFidelScore, decisionMakingScore, investKnowledgeScore));
		res.put(RISK_FID_KEY, getRiskFielityComment(investCondScore, riskScore,riskA1,riskA2));
		res.put(DECISION_MAKING_KEY, getDecisionMakingComment(investRuleScore, cogBiasScore));
		res.put(INVEST_KNOWLEDGE_KEY, getInvestKnowledgeComment(investKnowledgeScore));
	
		res.put(SIMILAR_TYPE_KEY, similarTypeList);
		
		return res;
	}
	
	
	
}
