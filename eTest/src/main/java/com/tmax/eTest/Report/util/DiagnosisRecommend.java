package com.tmax.eTest.Report.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tmax.eTest.Common.model.problem.DiagnosisCurriculum;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Report.exception.ReportBadRequestException;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class DiagnosisRecommend {
	
	public List<JsonArray> getRecommendLists(
			List<Pair<Problem, Integer>> choiceAndProbInfo,
			Map<String, Integer> scoreMap
			) throws Exception
	{
		List<JsonArray> result = new ArrayList<>();
		
		List<Pair<Problem, Integer>> commonProbChoiceInfo = new ArrayList<>();
		List<Pair<Problem, Integer>> knowledgeTypeProb = new ArrayList<>();
		List<Pair<Problem, Integer>> changeProbChoiceInfo = new ArrayList<>();
		List<Pair<Problem, Integer>> sellProbChoiceInfo = new ArrayList<>();
		
		
		for(Pair<Problem, Integer> probInfo : choiceAndProbInfo)
		{
			Problem prob = probInfo.getFirst();
			
			if(prob.getDiagnosisInfo() != null && prob.getDiagnosisInfo().getCurriculum() != null)
			{
				DiagnosisCurriculum curriculum = prob.getDiagnosisInfo().getCurriculum();
				
				switch(curriculum.getSubSection())
				{
				case "투자 상식":
					commonProbChoiceInfo.add(probInfo);
					break;
				case "종목고르기":
					knowledgeTypeProb.add(probInfo);
					break;
				case "가격변동 특징":
					changeProbChoiceInfo.add(probInfo);
					break;
				case "매매방법":
					sellProbChoiceInfo.add(probInfo);
					break;
				default:
					log.info("probDivideAndCalculateScores section invalid : " + curriculum.getSubSection());
					break;
				}
			}
			else
				log.info("probDivideAndCalculateScores prob not have diagnosisInfo : " + prob.toString());
		}
		
		result.add(getBasicRecommend(commonProbChoiceInfo));
		result.add(getAdvancedRecommend(knowledgeTypeProb, changeProbChoiceInfo, sellProbChoiceInfo));
		result.add(getTypeRecommend(scoreMap));
		
		return result;
	}
	
	private JsonArray getTypeRecommend(Map<String, Integer> scoreMap)
	{
		
		
		List<JsonObject> resultBucket = new ArrayList<>();
		
		int riskProfileScore = scoreMap.get(RuleBaseScoreCalculator.RISK_PROFILE_SCORE);
		int riskTracingScore = scoreMap.get(RuleBaseScoreCalculator.RISK_TRACING_SCORE);
		int riskProfileTypeIdx = (riskProfileScore >= 15)? 0
				:(riskProfileScore >= 11)? 1
				:2;
		int riskTracingTypeIdx = (riskTracingScore >= 12)? 0
				:(riskTracingScore >= 9)? 1
				:2;
		
		resultBucket.addAll(getInvestTypeRecommend(
				scoreMap.get(RuleBaseScoreCalculator.INVEST_SCORE),
				scoreMap.get(RuleBaseScoreCalculator.INVEST_TRACING),
				riskProfileTypeIdx,
				riskTracingTypeIdx,
				scoreMap.get(RuleBaseScoreCalculator.RISK_ANSWER_2_KEY)));
		resultBucket.addAll(getRiskTypeRecommend(riskProfileTypeIdx, riskTracingTypeIdx));
		
		return makeRandomResult(resultBucket);
	}
	
	private List<JsonObject> getRiskTypeRecommend(
			int riskProfileTypeIdx,
			int riskTracingTypeIdx)
	{
		List<JsonObject> resultBucket = new ArrayList<>();
		List<JsonObject> recIds = Arrays.asList(
				makeRecJsonObject("CV2021202020212012019999998", "video"), // 6-1", "video"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), // 6-2", "video"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), // 6-3", "video"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), // 6-4", "video"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), // 6-5", "video"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), // 6-6", "video"),
				makeRecJsonObject("CV2021202020212012019999998", "video") // 6-7", "video")
		);
		
		if(riskProfileTypeIdx != riskTracingTypeIdx)
		{
			resultBucket.add(recIds.get(0));
			resultBucket.add(recIds.get(1));
			resultBucket.add(recIds.get(2));
			resultBucket.add(recIds.get(3));
			resultBucket.add(recIds.get(4));
		}
		
		if(riskProfileTypeIdx == 2)
			resultBucket.add(recIds.get(5));
		else if(riskProfileTypeIdx == 0)
			resultBucket.add(recIds.get(6));
		
		return resultBucket;
	}
	
	private List<JsonObject> getInvestTypeRecommend(
			int investScore, 
			int investTracingScore,
			int riskProfileTypeIdx,
			int riskTracingTypeIdx,
			int riskAnsNum10)
	{
		List<JsonObject> resultBucket = new ArrayList<>();
		List<JsonObject> recIds = Arrays.asList(
				makeRecJsonObject("CV2021202020212012019999998", "video"), // 5-1", "video"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), // 5-2", "video"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), // 5-3", "video"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), // 5-4", "video"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), // 5-5", "video"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), // 5-6", "video"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), // 5-7", "video"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), // 5-8", "video"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), // 5-9", "video"),
				makeRecJsonObject("CV2021202020212012019999998", "video") // 5-10", "video")
		);
		
		if(investScore < 85)
		{
			resultBucket.add(recIds.get(0));
			resultBucket.add(recIds.get(1));
			resultBucket.add(recIds.get(2));
			resultBucket.add(recIds.get(3));
		}
		
		if(riskProfileTypeIdx == 0 || (riskProfileTypeIdx == 1 && riskTracingTypeIdx == 0))
			resultBucket.add(recIds.get(4));
		else if((riskProfileTypeIdx == 1 && riskTracingTypeIdx == 2) || riskProfileTypeIdx == 2)
			resultBucket.add(recIds.get(5));
		
		if(riskAnsNum10 <= 2)
			resultBucket.add(recIds.get(6));		// 단기 투자자 관련 질문
		else 
			resultBucket.add(recIds.get(7));		// 장기 투자자 관련 질문
		
		if(investTracingScore <= 36)
		{
			resultBucket.add(recIds.get(8));
			resultBucket.add(recIds.get(9));
		}
		
		return resultBucket;
	}
	
	private JsonArray getAdvancedRecommend(
			List<Pair<Problem, Integer>> typeProbChoiceInfo,
			List<Pair<Problem, Integer>> changeProbChoiceInfo,
			List<Pair<Problem, Integer>> sellProbChoiceInfo) 
			throws Exception
	{
		List<JsonObject> resultBucket = new ArrayList<>();
		
		
		resultBucket.addAll(getTypeRecommend(typeProbChoiceInfo));
		resultBucket.addAll(getChangeRecommend(changeProbChoiceInfo));
		resultBucket.addAll(getSellRecommend(sellProbChoiceInfo));
		
		return makeRandomResult(resultBucket);
	}
	
	String[] recommendIdsKey = {
		"1_high_corr", "1_high_incorr", "1_middle_corr", "1_middle_incorr", "1_low_corr", "1_low_incorr",	
		"2_high_corr", "2_high_incorr", "2_middle_corr", "2_middle_incorr", "2_low_corr", "2_low_incorr",	
		"3_high_corr", "3_high_incorr", "3_middle_corr", "3_middle_incorr", "3_low_corr", "3_low_incorr",	
	};
	
	// 
	private List<JsonObject> getRecommend(
			List<Pair<Problem, Integer>> probChoiceInfo,
			Map<String, List<JsonObject>> recommendIdList,
			int[] curriculumIds) throws Exception
	{
		List<JsonObject> result = new ArrayList<>();
		String[] probDiffStr = {"상", "중", "하"};
		
		for(Pair<Problem, Integer> probInfo : probChoiceInfo) {
			Problem prob = probInfo.getFirst();
			int choice = probInfo.getSecond();
			JsonArray solution = JsonParser.parseString(prob.getSolution()).getAsJsonArray();
			for(int i = 0; i < solution.size(); i++){
				JsonObject jo = solution.get(i).getAsJsonObject();
				if(jo.get("type") != null 
						&& jo.get("data") != null
						&& jo.get("type").getAsString().equals("MULTIPLE_CHOICE_CORRECT_ANSWER")){
					
					int currId = prob.getDiagnosisInfo().getCurriculumId();
					
					int colValue = (choice == jo.get("data").getAsInt())?0:1;
					int diffValue = -1;
					int currValue = -1;
					
					for(int idx = 0; idx < curriculumIds.length; idx++)
						if(currId == curriculumIds[idx])
							currValue = idx;
					
					for(int strIdx = 0; strIdx < probDiffStr.length; strIdx++)
						if(prob.getDifficulty().equals(probDiffStr[strIdx]))
							diffValue = strIdx;
					
					int keyValue = currValue * 6 + diffValue * 2 + colValue;
					
					if(diffValue == -1)
						throw new ReportBadRequestException("Difficult value error in getRecommend. probID : " 
								+ prob.getProbID() + " " + prob.getDifficulty());
					
					if(currValue == -1)
						throw new ReportBadRequestException("Difficult value error in getRecommend. probID : " 
								+ prob.getProbID()  + " " + currId);
					
					if(keyValue < 0 || keyValue > recommendIdsKey.length)
						throw new ReportBadRequestException("Key value error in getRecommend. probID : " 
								+ prob.getProbID()  + " keyValue : " + keyValue +
								" materials : " +colValue +" "+diffValue + " "+ currValue);
					
					result.addAll(recommendIdList.get(recommendIdsKey[keyValue]));
				}
			}
		}
		
		return result;
	}
	
	private JsonArray getBasicRecommend(List<Pair<Problem, Integer>> commonProbChoiceInfo) throws Exception
	{
		int[] curriIds = {13, 14, 15};
		List<JsonObject> recIds = Arrays.asList(
				makeRecJsonObject("CV2021202030344114259999993", "video"), //1-1", "video"),
				makeRecJsonObject("CV2021202030344114259999993", "video"), // 1-2", "video"),
				makeRecJsonObject("CV2021202030344114259999993", "video"), // 1-3", "video"),
				makeRecJsonObject("CV2021202030344114259999993", "video"), // 1-4", "video"),
				makeRecJsonObject("CV2021202030344114259999993", "video"), // 1-5", "video"),
				makeRecJsonObject("CV2021202030344114259999993", "video"), // 1-6", "video"),
				makeRecJsonObject("CV2021202030344114259999993", "video"), // 1-7", "video"),
				makeRecJsonObject("CV2021202030344114259999993", "video"), // 1-8", "video"),
				makeRecJsonObject("CV2021202030344114259999993", "video") // 1-9", "video")
		);
		List<List<Integer>> recoContentList = Arrays.asList(
				Arrays.asList(),
				Arrays.asList(),
				Arrays.asList(),
				Arrays.asList(8),
				Arrays.asList(),
				Arrays.asList(),
				
				Arrays.asList(),
				Arrays.asList(0,1,2,3),
				Arrays.asList(),
				Arrays.asList(),
				Arrays.asList(1,3),
				Arrays.asList(0,1,2,3),
				
				Arrays.asList(),
				Arrays.asList(4,5,6,7),
				Arrays.asList(7),
				Arrays.asList(4,5,6,7),
				Arrays.asList(4,7),
				Arrays.asList(4,5,6,7)
		);

		Map<String, List<JsonObject>> recommendIdList = new HashMap<>();		
		for(int keyIdx = 0; keyIdx < recommendIdsKey.length; keyIdx++)
		{
			List<JsonObject> input = new ArrayList<>();
			
			List<Integer> recIdIdxList = recoContentList.get(keyIdx);

			for(int idIdx : recIdIdxList)
				input.add(recIds.get(idIdx));
			
			recommendIdList.put(recommendIdsKey[keyIdx], input);
		}
		
		
		List<JsonObject> resultBucket = getRecommend(commonProbChoiceInfo, recommendIdList, curriIds);
		
		return makeRandomResult(resultBucket);
	}
	
	private List<JsonObject> getTypeRecommend(
			List<Pair<Problem, Integer>> typeProbChoiceInfo) 
			throws Exception
	{
		int[] curriIds = {16, 17, 18};
		List<JsonObject> recIds = Arrays.asList(
				makeRecJsonObject("CV2021202030344114259999993", "video"), //"단계 2-1", "video"),
				makeRecJsonObject("CV2021202030344114259999994", "video"), //"단계 2-2", "video"),
				makeRecJsonObject("CV2021202030344114259999994", "video"), //"단계 2-3", "video"),
				makeRecJsonObject("CV2021202030344114259999994", "video"), //"단계 2-4", "video"),
				makeRecJsonObject("CV2021202030344114259999994", "video"), //"단계 2-5", "video"),
				makeRecJsonObject("CV2021202030344114259999996", "video"), //"단계 2-6", "video"),
				makeRecJsonObject("CV2021202030344114259999996", "video"), //"단계 2-7", "video"),
				makeRecJsonObject("CV2021202030344114259999996", "video"), //"단계 2-8", "video"),
				makeRecJsonObject("CV2021202030344114259999996", "video") //"단계 2-9", "video")
		);
		List<List<Integer>> recoContentList = Arrays.asList(
				Arrays.asList(),
				Arrays.asList(0,1,2),
				Arrays.asList(2),
				Arrays.asList(0,1,2),
				Arrays.asList(1,2),
				Arrays.asList(0,1,2),
				
				Arrays.asList(),
				Arrays.asList(3,4,5),
				Arrays.asList(5),
				Arrays.asList(3,4,5),
				Arrays.asList(4,5),
				Arrays.asList(3,4,5),
				
				Arrays.asList(),
				Arrays.asList(6),
				Arrays.asList(6),
				Arrays.asList(6,7,8),
				Arrays.asList(6,8),
				Arrays.asList(6,7,8)
		);

		Map<String, List<JsonObject>> recommendIdList = new HashMap<>();		
		for(int keyIdx = 0; keyIdx < recommendIdsKey.length; keyIdx++)
		{
			List<JsonObject> input = new ArrayList<>();
			
			List<Integer> recIdIdxList = recoContentList.get(keyIdx);

			for(int idIdx : recIdIdxList)
				input.add(recIds.get(idIdx));
			
			recommendIdList.put(recommendIdsKey[keyIdx], input);
		}
		
		
		List<JsonObject> resultBucket = getRecommend(typeProbChoiceInfo, recommendIdList, curriIds);
		
		return resultBucket;
	}
	
	private List<JsonObject> getChangeRecommend(
			List<Pair<Problem, Integer>> changeProbChoiceInfo) 
			throws Exception
	{
		int[] curriIds = {19, 20, 21};
		List<JsonObject> recIds = Arrays.asList(
				makeRecJsonObject("CV2021202030344114259999997", "video"), // 3-1", "video"),
				makeRecJsonObject("CV2021202030344114259999997", "video"), // 3-2", "video"),
				makeRecJsonObject("CV2021202030344114259999997", "video"), // 3-3", "video"),
				makeRecJsonObject("CV2021202030344114259999997", "video"), // 3-4", "video"),
				makeRecJsonObject("CV2021202030344114259999997", "video"), // 3-5", "video"),
				makeRecJsonObject("CV2021102030344114259999998", "video"), // 3-6", "video"),
				makeRecJsonObject("CV2021102030344114259999998", "video"), // 3-7", "video"),
				makeRecJsonObject("CV2021102030344114259999998", "video"), // 3-8", "video"),
				makeRecJsonObject("CV2021102030344114259999998", "video") // 3-9", "video")
		);
		List<List<Integer>> recoContentList = Arrays.asList(
				Arrays.asList(),
				Arrays.asList(0,1,2),
				Arrays.asList(2),
				Arrays.asList(0,1,2),
				Arrays.asList(1,2),
				Arrays.asList(0,1,2),
				
				Arrays.asList(),
				Arrays.asList(3,4,5),
				Arrays.asList(5),
				Arrays.asList(3,4,5),
				Arrays.asList(4,5),
				Arrays.asList(3,4,5),
				
				Arrays.asList(),
				Arrays.asList(6,7,8),
				Arrays.asList(6),
				Arrays.asList(6,7,8),
				Arrays.asList(7,8),
				Arrays.asList(6,7,8)
		);

		Map<String, List<JsonObject>> recommendIdList = new HashMap<>();		
		for(int keyIdx = 0; keyIdx < recommendIdsKey.length; keyIdx++)
		{
			List<JsonObject> input = new ArrayList<>();
			
			List<Integer> recIdIdxList = recoContentList.get(keyIdx);

			for(int idIdx : recIdIdxList)
				input.add(recIds.get(idIdx));
			
			recommendIdList.put(recommendIdsKey[keyIdx], input);
		}
		
		List<JsonObject> resultBucket = getRecommend(changeProbChoiceInfo, recommendIdList, curriIds);
		
		return resultBucket;
	}
	
	private List<JsonObject> getSellRecommend(
			List<Pair<Problem, Integer>> sellProbChoiceInfo) 
			throws Exception
	{
		int[] curriIds = {22, 23, 24};
		List<JsonObject> recIds = Arrays.asList(
				makeRecJsonObject("CV2021202020212012019999998", "video"), //"보카1", "article"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), //"보카2", "article"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), //"보카3", "article"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), //"보카4", "article"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), //"보카5", "article"),
				makeRecJsonObject("CV2021202020212012019999998", "video"), //"보카6", "article"),
				makeRecJsonObject("CV2021202030344114259999993", "video"), //"보카7", "article"),
				makeRecJsonObject("CV2021202030344114259999993", "video"), //"보카8", "article"),
				makeRecJsonObject("CV2021202030344114259999993", "video") //"보카9", "article")
		);
		List<List<Integer>> recoContentList = Arrays.asList(
				Arrays.asList(),
				Arrays.asList(0,1,2),
				Arrays.asList(2),
				Arrays.asList(0,1,2),
				Arrays.asList(1,2),
				Arrays.asList(0,1,2),
				
				Arrays.asList(),
				Arrays.asList(3,4,5),
				Arrays.asList(5),
				Arrays.asList(3,4,5),
				Arrays.asList(4,5),
				Arrays.asList(3,4,5),
				
				Arrays.asList(),
				Arrays.asList(6,7,8),
				Arrays.asList(8),
				Arrays.asList(6,7,8),
				Arrays.asList(7,8),
				Arrays.asList(6,7,8)
		);

		Map<String, List<JsonObject>> recommendIdList = new HashMap<>();		
		for(int keyIdx = 0; keyIdx < recommendIdsKey.length; keyIdx++)
		{
			List<JsonObject> input = new ArrayList<>();
			
			List<Integer> recIdIdxList = recoContentList.get(keyIdx);

			for(int idIdx : recIdIdxList)
				input.add(recIds.get(idIdx));
			
			recommendIdList.put(recommendIdsKey[keyIdx], input);
		}
		
		List<JsonObject> resultBucket = getRecommend(sellProbChoiceInfo, recommendIdList, curriIds);
		
		return resultBucket;
	}
	
	private JsonArray makeRandomResult(List<JsonObject> resultBucket)
	{
		JsonArray result = new JsonArray();
		
		if(resultBucket.size() < 5)
			for(JsonObject obj : resultBucket)
				result.add(obj);
		else
		{
			Random rand = new Random();
			
			for(int i = 0; i < 5; i++)
			{
				int idx = rand.nextInt(resultBucket.size());
				result.add(resultBucket.get(idx));
				resultBucket.remove(idx);
			}
		}
		
		return result;
	}
	
	private JsonObject makeRecJsonObject(String id, String type)
	{
		JsonObject res = new JsonObject();
		res.addProperty("id", id);
		res.addProperty("type", type);
		
		return res;
	}
}
