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
import com.tmax.eTest.Report.util.DiagnosisUtil.AnswerKey;
import com.tmax.eTest.Report.util.DiagnosisUtil.Chapter;
import com.tmax.eTest.Report.util.DiagnosisUtil.KnowledgeSection;
import com.tmax.eTest.Report.util.DiagnosisUtil.RiskProfile;
import com.tmax.eTest.Report.util.DiagnosisUtil.ScoreKey;
import com.tmax.eTest.Report.util.DiagnosisUtil.TendencySection;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
// Rule Base 점수, Triton 점수 관련 Method 집합 Class
public class RuleBaseScoreCalculator {
	
	final private boolean DEBUG_LOG = false;
	
	private void debugLog(String str)
	{
		if(DEBUG_LOG)
			log.info(str);
	}


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
		List<Pair<Problem, Integer>> knowledgeBasicProb = new ArrayList<>(); // 주식상식
		List<Pair<Problem, Integer>> knowledgeTypeProb = new ArrayList<>(); // 종목고르기
		List<Pair<Problem, Integer>> knowledgeChangeProb = new ArrayList<>(); // 가격변동특징
		List<Pair<Problem, Integer>> knowledgeSellProb = new ArrayList<>(); // 매매방법

		// probInfo<문제, 선택한 답안 번호>
		for(Pair<Problem, Integer> probInfo : probInfos)
		{
			Problem prob = probInfo.getFirst();
			
			if(prob.getDiagnosisInfo() != null && prob.getDiagnosisInfo().getCurriculum() != null)
			{
				DiagnosisCurriculum curriculum = prob.getDiagnosisInfo().getCurriculum();
				String chapter = curriculum.getChapter();
				String section = curriculum.getSection();
				String subSection = curriculum.getSubSection();
				int ansScore = getProbScoreByAnswerNum(prob, probInfo.getSecond());
				
				if(chapter.equals(Chapter.KNOWLEDGE.toString()))
				{
					if(section.equals(KnowledgeSection.BASIC.toString()))
					{
						knowledgeBasicProb.add(probInfo);
						res.put(subSection, ansScore);
					}
					else if(section.equals(KnowledgeSection.TYPE_SELECT.toString()))
						knowledgeTypeProb.add(probInfo);
					else if(section.equals(KnowledgeSection.PRICE_CHANGE.toString()))
						knowledgeChangeProb.add(probInfo);
					else if(section.equals(KnowledgeSection.SELL_WAY.toString()))
						knowledgeSellProb.add(probInfo);
					else
						log.info("probDivideAndCalculateScores, in knowledge chapter. section invalid : " + section);
				}
				else // 성향
				{
					if(section.equals(TendencySection.RISK_TRACING.toString()))
						riskTracingProb.add(probInfo);
					else if(section.equals(TendencySection.RISK_PROFILE.toString()))
						if(curriculum.getSubSection().equals("리스크 감내역량"))
							riskPatiProb.add(probInfo);
						else
							riskLevelProb.add(probInfo);
					else if(section.equals(TendencySection.INVEST_TRACING.toString()))
						investTracingProb.add(probInfo);
					else if(section.equals(TendencySection.INVEST_PROFILE.toString()))
						investProfileProb.add(probInfo);

					else
					{
						log.info("probDivideAndCalculateScores, in tendency chapter. section invalid : " + section);
						continue;
					}
					res.put(subSection, ansScore);
				}
			}
			else
			{
				log.info("probDivideAndCalculateScores prob not have diagnosisInfo : " + prob.toString());
			}
		}
		
		debugLog("Invest tracing Prob Num : "+investTracingProb.size());
		debugLog("Invest profile Prob Num : "+investProfileProb.size());
		debugLog("Risk tracing Prob Num : "+riskTracingProb.size());
		debugLog("Risk 감내역량 Prob Num : "+riskPatiProb.size());
		debugLog("Risk 감내 수준 Prob Num : "+riskLevelProb.size());
		debugLog("Knowledge 투자기초 Prob Num : "+knowledgeBasicProb.size());
		debugLog("Knowledge 종목고르기 Prob Num : "+knowledgeTypeProb.size());
		debugLog("Knowledge 가격 변동 특징 Prob Num : "+knowledgeChangeProb.size());
		debugLog("Knowledge 매매방법 Prob Num : "+knowledgeSellProb.size());
		
		
		if(riskPatiProb.size() != 2) // 2문항
		{
			log.info("probDivideAndCalculateScores riskPatienceProb size error : " + riskPatiProb.size());
			res.put(AnswerKey.RISK_1.toString(), 1);
			res.put(AnswerKey.RISK_2.toString(), 1);
		}
		else
		{
			int q1Idx = riskPatiProb.get(0).getFirst().getProbID() < riskPatiProb.get(1).getFirst().getProbID()?0:1;
			int q2Idx = q1Idx==0?1:0;
			
			res.put(AnswerKey.RISK_1.toString(), riskPatiProb.get(q1Idx).getSecond());
			res.put(AnswerKey.RISK_2.toString(), riskPatiProb.get(q2Idx).getSecond());
		}
		
		res.putAll(calculateRiskScore(riskTracingProb, riskPatiProb, riskLevelProb));
		res.putAll(calculateDecisionMakingScore(investTracingProb, investProfileProb));
		res.putAll(calculateInvestKnowledgeScoreV2(
				knowledgeBasicProb, 
				knowledgeTypeProb,
				knowledgeChangeProb,
				knowledgeSellProb));
		res.put(ScoreKey.GI.toString(), Double.valueOf(res.get(ScoreKey.RISK.toString()) * 0.3
				+ res.get(ScoreKey.INVEST.toString()) * 0.3
				+ res.get(ScoreKey.KNOWLEDGE.toString())* 0.4).intValue());
	
		
		return res;
	}
	
	private int getProbScoreByAnswerNum(Problem prob, int answerNum)
	{
		int score = 0;
		
		for(ProblemChoice choice : prob.getProblemChoices())
		{
			if(choice.getChoiceNum() == answerNum)
			{
				score = choice.getChoiceScore();
				break;
			}
		}
		
		return score;
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
		
		debugLog("in calculateRiskScore tracing, profile :"+riskTracingScore+" "+riskProfileScore);
		debugLog("in calculateRiskScore capa, level :"+riskCapaScore+" "+riskLevelScore);
		debugLog("in calculateRiskScore temp, risk :"+tempScore+" "+riskScore);
		
		for(Pair<Problem, Integer> temp : riskCapaProbList)
			debugLog("in calculateRiskScore riskCapaProbList :"+temp.getFirst().getProbID()+" "+temp.getSecond());
		for(Pair<Problem, Integer> temp : riskLevelProbList)
			debugLog("in calculateRiskScore riskLevelProbList :"+temp.getFirst().getProbID()+" "+temp.getSecond());

		res.put(ScoreKey.RISK.toString(), riskScore);
		res.put(ScoreKey.RISK_TRACING.toString(), riskTracingScore);
		res.put(ScoreKey.RISK_PROFILE.toString(), riskProfileScore);
		res.put(RiskProfile.CAPACITY.toString(), riskCapaScore);
		res.put(RiskProfile.LEVEL.toString(), riskProfileScore);

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

		res.put(TendencySection.INVEST_TRACING.toString(), tracingScore);
		res.put(TendencySection.INVEST_PROFILE.toString(), profileScore);
		res.put(ScoreKey.INVEST.toString(), tracingScore + profileScore);

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
		
		res.put(ScoreKey.KNOWLEDGE.toString(), knowledgeScore);
		res.put(KnowledgeSection.BASIC.toString(), commonScore);
		res.put(KnowledgeSection.TYPE_SELECT.toString(), typeScore);
		res.put(KnowledgeSection.SELL_WAY.toString(), sellScore);
		res.put(KnowledgeSection.PRICE_CHANGE.toString(), changeScore);
		
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
					debugLog("in makeScore :"+prob.getFirst().getProbID()+" "+ prob.getSecond()+" "+choice.getChoiceScore());
					break;
				}
			}
		}
		
		return res;
	}	
	
}
