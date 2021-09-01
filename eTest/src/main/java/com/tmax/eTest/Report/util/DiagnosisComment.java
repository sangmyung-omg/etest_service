package com.tmax.eTest.Report.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class DiagnosisComment {
	
	final public static String TOTAL_RES_KEY = "totalResult";
	final public static String RISK_FID_KEY = "riskFidelity";
	final public static String DECISION_MAKING_KEY = "decisionMaking";
	final public static String INVEST_KNOWLEDGE_KEY = "investKnowledge";
	final public static String SIMILAR_TYPE_KEY = "similarType";
	final public static String SELF_DIAG_TYPE_KEY = "selfDiagType";
	
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
	

	public Map<String, String> makeInvestMainComment(int profileScore, int tracingScore)
	{
		String[] investType = {
				"투자원칙이 명확하고 이성적이며 행동편향이 낮은 투자자",
				"투자원칙이 명확하고 이성적이며 행동편향이 낮은 투자자",
				"투자원칙은 확고하지 않지만 이성적이며 행동편향은 보통인 투자자",
				"투자원칙은 확고하지 않지만 이성적이며 행동편향은 보통인 투자자",
				"투자원칙은 없고 행동편향은 높지만 이성적인 투자자",
				"투자원칙이 명확하고 이성적이며 행동편향이 낮은 투자자",
				"투자원칙이 명확하고 이성적이며 행동편향이 낮은 투자자",
				"투자원칙은 확고하지 않지만 이성적이며 행동편향은 보통인 투자자",
				"투자원칙은 확고하지 않지만 이성적이며 행동편향은 높은 투자자",
				"투자원칙은 없고 행동편향은 높지만 이성적인 투자자",
				"투자원칙은 명확하고 행동편향이 낮지만 감정적인 투자자",
				"투자원칙은 명확하지만 감정적이며 행동편향은 보통인 투자자",
				"투자원칙은 확고하지 않고 감정적이며 행동편향은 보통인 투자자",
				"투자원칙은 확고하지 않고 감정적이며 행동편향이 높은 투자자",
				"투자원칙은 없고 감정적이며 행동편향이 매우 높은 투자자",
				"투자원칙은 명확하고 행동편향이 낮지만 감정적인 투자자",
				"투자원칙은 명확하지만 감정적이며 행동편향은 보통인 투자자",
				"투자원칙은 확고하지 않고 감정적이며 행동편향이 높은 투자자",
				"투자원칙은 확고하지 않고 감정적이며 행동편향이 높은 투자자",
				"투자원칙은 없고 감정적이며 행동편향이 매우 높은 투자자",
				"투자원칙은 명확하지만 보통의 행동편향을 가진 충동적인 투자자",
				"투자원칙은 명확하지만 보통의 행동편향을 가진 충동적인 투자자",
				"투자원칙은 확고하지 않고 충동적이며 행동편향이 높은 투자자",
				"투자원칙은 확고하지 않고 충동적이며 행동편향이 매우 높은 투자자",
				"투자원칙은 없고 충동적이며 행동편향이 매우 높은 투자자",
				"투자원칙은 명확하지만 보통의 행동편향을 가진 충동적인 투자자",
				"투자원칙은 명확하지만 충동적이고 행동편향이 높은 투자자",
				"투자원칙은 확고하지 않고 충동적이며 행동편향이 높은 투자자",
				"투자원칙은 확고하지 않고 충동적이며 행동편향이 매우 높은 투자자",
				"투자원칙은 없고 충동적이며 행동편향이 매우 높은 투자자"};
		
		String[] investMainComment = {
				"투자원칙이 명확하고 이성적이며 행동편향이 낮은 투자자",
				"투자원칙이 명확하고 이성적이며 행동편향이 낮은 투자자",
				"투자원칙은 확고하지 않지만 이성적이며 행동편향은 보통인 투자자",
				"투자원칙은 확고하지 않지만 이성적이며 행동편향은 보통인 투자자",
				"투자원칙은 없고 행동편향은 높지만 이성적인 투자자",
				"투자원칙이 명확하고 이성적이며 행동편향이 낮은 투자자",
				"투자원칙이 명확하고 이성적이며 행동편향이 낮은 투자자",
				"투자원칙은 확고하지 않지만 이성적이며 행동편향은 보통인 투자자",
				"투자원칙은 확고하지 않지만 이성적이며 행동편향은 높은 투자자",
				"투자원칙은 없고 행동편향은 높지만 이성적인 투자자",
				"투자원칙은 명확하고 행동편향이 낮지만 감정적인 투자자",
				"투자원칙은 명확하지만 감정적이며 행동편향은 보통인 투자자",
				"투자원칙은 확고하지 않고 감정적이며 행동편향은 보통인 투자자",
				"투자원칙은 확고하지 않고 감정적이며 행동편향이 높은 투자자",
				"투자원칙은 없고 감정적이며 행동편향이 매우 높은 투자자",
				"투자원칙은 명확하고 행동편향이 낮지만 감정적인 투자자",
				"투자원칙은 명확하지만 감정적이며 행동편향은 보통인 투자자",
				"투자원칙은 확고하지 않고 감정적이며 행동편향이 높은 투자자",
				"투자원칙은 확고하지 않고 감정적이며 행동편향이 높은 투자자",
				"투자원칙은 없고 감정적이며 행동편향이 매우 높은 투자자",
				"투자원칙은 명확하지만 보통의 행동편향을 가진 충동적인 투자자",
				"투자원칙은 명확하지만 보통의 행동편향을 가진 충동적인 투자자",
				"투자원칙은 확고하지 않고 충동적이며 행동편향이 높은 투자자",
				"투자원칙은 확고하지 않고 충동적이며 행동편향이 매우 높은 투자자",
				"투자원칙은 없고 충동적이며 행동편향이 매우 높은 투자자",
				"투자원칙은 명확하지만 보통의 행동편향을 가진 충동적인 투자자",
				"투자원칙은 명확하지만 충동적이고 행동편향이 높은 투자자",
				"투자원칙은 확고하지 않고 충동적이며 행동편향이 높은 투자자",
				"투자원칙은 확고하지 않고 충동적이며 행동편향이 매우 높은 투자자",
				"투자원칙은 없고 충동적이며 행동편향이 매우 높은 투자자"};
		
		Map<String, String> result = new HashMap<>();
		
		int profileIdx = (profileScore >= 45) ? 0 	// 리스크 선호형
				: (profileScore >= 40) ? 1			// 리스크 중립형
				: (profileScore >= 35) ? 2	
				: (profileScore >= 30) ? 3	
				: (profileScore >= 25) ? 4	
				: 5;								// 리스크 회피형
		
		int tracingIdx = (tracingScore >= 55) ? 0	// 집중 투자형
				: (tracingScore >= 45) ? 1	
				: (tracingScore >= 36) ? 2	
				: (tracingScore >= 26) ? 3			// 중간 투자형
				: 4;								// 분산 투자형
		
		int finalIdx = profileIdx * 5 + tracingIdx;
		
		result.put("main", investType[finalIdx]);
		result.put("detail", investMainComment[finalIdx]);
		
		return result;
	}
	
	public List<Object> makeInvestDetailComment(int profileScore, int tracingScore)
	{
		
		String[] profileMainList = {"이성적인 투자자", "감정적인 투자자", "충동적인 투자자"};
		String[] tracingMainList = {"투자원칙 명확", "투자원칙 확고하지 않음", "투자원칙 없음"};
		
		String[] profileDetailList = {"이성적인 투자자", "감정적인 투자자", "충동적인 투자자"};
		String[] tracingDetailList = {"투자원칙 명확", "투자원칙 확고하지 않음", "투자원칙 없음"};
		
		int profileIdx = (profileScore >= 40) ? 0 	// 이성
				: (profileScore >= 30) ? 1			// 감성
				: 2;								// 충동
		
		int tracingIdx = (tracingScore >= 45) ? 0	// 명확
				: (tracingScore >= 26) ? 1			// 확고하지 않음
				: 2;								// 없음
		
		int stretchProfileScore = (int)((profileScore - 20) / 25.f * 100);
		int stretchTracingScore = (int)((tracingScore - 17) / 38.f * 100);
		
		List<Object> result = new ArrayList<>();
		
		Map<String, Object> profileCommentInfo = new HashMap<>();
		Map<String, Object> tracingCommentInfo = new HashMap<>();
		
		profileCommentInfo.put("name", "행동편향");
		profileCommentInfo.put("main", profileMainList[profileIdx]);
		profileCommentInfo.put("detail", profileDetailList[profileIdx]);
		profileCommentInfo.put("score", stretchProfileScore);
		
		tracingCommentInfo.put("name", "투자원칙");
		tracingCommentInfo.put("main", tracingMainList[tracingIdx]);
		tracingCommentInfo.put("detail", tracingDetailList[tracingIdx]);
		tracingCommentInfo.put("score", stretchTracingScore);
		
		result.add(profileCommentInfo);
		result.add(tracingCommentInfo);
		
		return result;
	}
	
	public Map<String, String> makeKnowledgeMainComment(int knowledgeScore)
	{
		Map<String, String> result = new HashMap<>();
		int[] rankMinValue = {85, 70, 55, 40, 25};
		int knowledgeScoreIdx = 0;
		
		String[] knowledgeMain = {"우수", "양호", "보통", "노력요함", "부족"};
		String[] knowledgeDetail = {"우수", "양호", "보통", "노력요함", "부족"};
		
		
		for(int i = 0; i < rankMinValue.length; i++)
			if(knowledgeScore < rankMinValue[i])
				knowledgeScoreIdx = i+1;
		
		result.put("main", knowledgeMain[knowledgeScoreIdx]);
		result.put("detail", knowledgeDetail[knowledgeScoreIdx]);
		
		return result;
	}
	
	public List<Object> makeKnowledgeDetailComment(
			int commonScore, 
			int typeScore,
			int changeScore,
			int sellScore)
	{
		List<Object> result = new ArrayList<>();
		
		String[] commonCommentList = {"우수", "양호", "보통", "노력요함", "부족"};
		String[] actualCommentList = {"우수", "양호", "보통", "노력요함", "부족"};
		Map<String, Object> commonCommentInfo = new HashMap<>();
		Map<String, Object> actualCommentInfo = new HashMap<>();
		int stretchCommonScore = (int) (commonScore / 22.f * 100);
		int stretchActualScore = (int) ((typeScore + changeScore + sellScore) / 72.f * 100);
		int[] rankMinValue = {89, 74, 58, 42, 26};
		
		int commonIdx = 0;
		int actualIdx = 0;
		
		for(int i = 0; i < rankMinValue.length; i++)
		{
			if(stretchCommonScore < rankMinValue[i])
				commonIdx = i+1;
			if(stretchActualScore < rankMinValue[i])
				actualIdx = i+1;
		}
		
		commonCommentInfo.put("name", "투자 기초");
		//commonCommentInfo.put("main", "");
		commonCommentInfo.put("detail", commonCommentList[commonIdx]);
		commonCommentInfo.put("score", stretchCommonScore);
		
		actualCommentInfo.put("name", "투자 실전");
		//actualCommentInfo.put("main", "");
		actualCommentInfo.put("detail", actualCommentList[actualIdx]);
		actualCommentInfo.put("score", stretchActualScore);
		
		result.add(commonCommentInfo);
		result.add(actualCommentInfo);
		
		return result;
	}
	
	
	
	public Map<String, String> makeRiskMainComment(int profileScore, int tracingScore)
	{
		String[] riskType = {
				"전형적인 공격형 투자자",
				"2% 부족한 공격형 투자자",
				"엇박자 공격형 투자자",
				"하이브리드 공격형 투자자",
				"전형적인 하이브리드형 투자자",
				"하이브리드 방어형 투자자",
				"엇박자 방어형 투자자",
				"2% 부족한 방어형 투자자",
				"전형적인 방어형 투자자"};
		String[] riskMainComment = {
				"전형적인 공격형 투자자",
				"2% 부족한 공격형 투자자",
				"엇박자 공격형 투자자",
				"하이브리드 공격형 투자자",
				"전형적인 하이브리드형 투자자",
				"하이브리드 방어형 투자자",
				"엇박자 방어형 투자자",
				"2% 부족한 방어형 투자자",
				"전형적인 방어형 투자자"};
		
		Map<String, String> result = new HashMap<>();
		
		int profileIdx = (profileScore >= 15) ? 0 	// 리스크 선호형
				: (profileScore >= 11) ? 1			// 리스크 중립형
				: 2;								// 리스크 회피형
		
		int tracingIdx = (tracingScore >= 12) ? 0	// 집중 투자형
				: (tracingScore >= 9) ? 1			// 중간 투자형
				: 2;								// 분산 투자형
		
		int finalIdx = profileIdx * 3 + tracingIdx;
		
		result.put("main", riskType[finalIdx]);
		result.put("detail", riskMainComment[finalIdx]);
		
		return result;
	}
	
	public List<Object> makeRiskDetailComment(int profileScore, int tracingScore)
	{
		String[] profileMainList = {"리스크 선호형", "리스크 중립형", "리스크 회피형"};
		String[] tracingMainList = {"집중형 투자자", "중립형 투자자", "분산형 투자자"};
		
		String[] profileDetailList = {"리스크 선호형", "리스크 중립형", "리스크 회피형"};
		String[] tracingDetailList = {"집중형 투자자", "중립형 투자자", "분산형 투자자"};
		
		int profileIdx = (profileScore >= 15) ? 0 	// 리스크 선호형
				: (profileScore >= 11) ? 1			// 리스크 중립형
				: 2;								// 리스크 회피형
		
		int tracingIdx = (tracingScore >= 12) ? 0	// 집중 투자형
				: (tracingScore >= 9) ? 1			// 중간 투자형
				: 2;								// 분산 투자형
		
		int stretchProfileScore = (int)((profileScore - 5) / 15.f * 100);
		int stretchTracingScore = (int)((tracingScore - 4) / 12.f * 100);
		
		List<Object> result = new ArrayList<>();
		
		Map<String, Object> profileCommentInfo = new HashMap<>();
		Map<String, Object> tracingCommentInfo = new HashMap<>();
		
		profileCommentInfo.put("name", "투자위험 태도");
		profileCommentInfo.put("main", profileMainList[profileIdx]);
		profileCommentInfo.put("detail", profileDetailList[profileIdx]);
		profileCommentInfo.put("score", stretchProfileScore);
		
		tracingCommentInfo.put("name", "투자 방법");
		tracingCommentInfo.put("main", tracingMainList[tracingIdx]);
		tracingCommentInfo.put("detail", tracingDetailList[tracingIdx]);
		tracingCommentInfo.put("score", stretchTracingScore);
		
		result.add(profileCommentInfo);
		result.add(tracingCommentInfo);
		
		return result;
	}

	public List<String> makeSimilarTypeInfo(int riskScore, int investScore, int knowledgeScore) {
		List<String> res = new ArrayList<>();
		
		int totalScore = riskScore >= 75 ? 3 : riskScore >= 55 ? 2 : 1;
		totalScore += investScore >= 75 ? 3 : investScore >= 55 ? 2 : 1;
		totalScore += knowledgeScore >= 70 ? 3 : knowledgeScore >= 50 ? 2 : 1;
				
		String investerRatio = totalScore == 9 || totalScore == 3 ? "3.7%"
				: totalScore > 5 ? "33.33%" : "25.93%";
		String avgItemNum = riskScore >= 75 ? "8종목" : riskScore >= 55 ? "5종목" : "3종목";
		String investRatio = riskScore >= 75 ? "55%" : riskScore >= 55 ? "40%" : "10%";
		
		res.add(investerRatio);
		res.add(avgItemNum);
		res.add(investRatio);
		
		return res;
	}
	
}
