package com.tmax.eTest.Report.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tmax.eTest.Common.model.problem.DiagnosisCurriculum;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.video.Video;
import com.tmax.eTest.Common.repository.video.VideoRepository;
import com.tmax.eTest.Report.exception.ReportBadRequestException;
import com.tmax.eTest.Report.util.DiagnosisUtil.AnswerKey;
import com.tmax.eTest.Report.util.DiagnosisUtil.KnowledgeSection;
import com.tmax.eTest.Report.util.DiagnosisUtil.ScoreKey;
import com.tmax.eTest.Report.util.DiagnosisUtil.TendencySection;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class DiagnosisRecommend {
	
	@Autowired
	VideoRepository videoRepo;
	
	final List<String> Q1_RELATED = Arrays.asList(
			"질문1-1","질문1-2","질문1-3","질문1-4","질문1-5","질문1-6","질문1-7","질문1-8","질문1-9");
	final List<String> Q2_RELATED = Arrays.asList(
			"단계2-1","단계2-2","단계2-3","단계2-4","단계2-5","단계2-6","단계2-7","단계2-8","단계2-9");
	final List<String> Q3_RELATED = Arrays.asList(
			"질문3-1","질문3-2","질문3-3","질문3-4","질문3-5","질문3-6","질문3-7","질문3-8","질문3-9");
	final List<String> VOCA_RELATED = Arrays.asList(
			"보카1","보카2","보카3","보카4","보카5","보카6","보카7","보카8","보카9","보카0");
	final List<String> Q5_RELATED = Arrays.asList(
			"질문5-1","질문5-2","질문5-3","질문5-4","질문5-5","질문5-6","질문5-7","질문5-8","질문5-9","질문5-10");
	final List<String> Q6_RELATED = Arrays.asList(
			"질문6-1","질문6-2","질문6-3","질문6-4","질문6-5","질문6-6","질문6-7");
	
	final int[] INVEST_BASIC_CURR_ID = {16, 17, 18};
	final int[] CHOICE_STOCK_CURR_ID = {19, 20, 21};
	final int[] PRICE_CHANGE_CURR_ID = {22, 23, 24};
	final int[] SELL_WAY_CURR_ID = {25, 26, 27};
	
	public List<JsonArray> getRecommendLists(
			List<Pair<Problem, Integer>> choiceAndProbInfo,
			Map<String, Integer> scoreMap
			) throws Exception
	{
		List<JsonArray> result = new ArrayList<>();
		
		List<Pair<Problem, Integer>> commonProbChoiceInfo = new ArrayList<>();
		List<Pair<Problem, Integer>> typeSelectProbChoiceInfo = new ArrayList<>();
		List<Pair<Problem, Integer>> changeProbChoiceInfo = new ArrayList<>();
		List<Pair<Problem, Integer>> sellProbChoiceInfo = new ArrayList<>();
		
		
		for(Pair<Problem, Integer> probInfo : choiceAndProbInfo)
		{
			Problem prob = probInfo.getFirst();
			
			if(prob.getDiagnosisInfo() != null && prob.getDiagnosisInfo().getCurriculum() != null)
			{
				DiagnosisCurriculum curriculum = prob.getDiagnosisInfo().getCurriculum();
				String sectionName = curriculum.getSection();
				
				if(sectionName.equals(KnowledgeSection.BASIC.toString()))
					commonProbChoiceInfo.add(probInfo);
				else if(sectionName.equals(KnowledgeSection.TYPE_SELECT.toString()))
					typeSelectProbChoiceInfo.add(probInfo);
				else if(sectionName.equals(KnowledgeSection.PRICE_CHANGE.toString()))
					changeProbChoiceInfo.add(probInfo);
				else if(sectionName.equals(KnowledgeSection.SELL_WAY.toString()))
					sellProbChoiceInfo.add(probInfo);
			}
			else
				log.info("probDivideAndCalculateScores prob not have diagnosisInfo : " + prob.toString());
		}
		
		result.add(getBasicRecommend(commonProbChoiceInfo));
		result.add(getAdvancedRecommend(typeSelectProbChoiceInfo, changeProbChoiceInfo, sellProbChoiceInfo));
		result.add(getTypeRecommend(scoreMap));
		
		return result;
	}
	
	private JsonArray getTypeRecommend(Map<String, Integer> scoreMap)
	{
		
		
		List<JsonObject> resultBucket = new ArrayList<>();
		
		int riskProfileScore = scoreMap.get(ScoreKey.RISK_PROFILE.toString());
		int riskTracingScore = scoreMap.get(ScoreKey.RISK_TRACING.toString());
		int riskProfileTypeIdx = (riskProfileScore >= 15)? 0
				:(riskProfileScore >= 11)? 1
				:2;
		int riskTracingTypeIdx = (riskTracingScore >= 12)? 0
				:(riskTracingScore >= 9)? 1
				:2;
		
		resultBucket.addAll(getInvestTypeRecommend(
				scoreMap.get(ScoreKey.INVEST.toString()),
				scoreMap.get(TendencySection.INVEST_TRACING.toString()),
				riskProfileTypeIdx,
				riskTracingTypeIdx,
				scoreMap.get(AnswerKey.RISK_2.toString())));
		resultBucket.addAll(getRiskTypeRecommend(riskProfileTypeIdx, riskTracingTypeIdx));
		
		return makeRandomResult(resultBucket);
	}
	
	private List<JsonObject> getRiskTypeRecommend(
			int riskProfileTypeIdx,
			int riskTracingTypeIdx)
	{
		
		List<String> relatedList = new ArrayList<>();
		
		// 자가진단 코멘트 및 추천 콘텐츠 정리 20210902 추천코멘트(고객작성) #위험관리 규칙 참고
		if(riskProfileTypeIdx != riskTracingTypeIdx)
		{
			relatedList.add(Q6_RELATED.get(0));
			relatedList.add(Q6_RELATED.get(1));
			relatedList.add(Q6_RELATED.get(2));
			relatedList.add(Q6_RELATED.get(3));
			relatedList.add(Q6_RELATED.get(4));
		}
		
		if(riskProfileTypeIdx == 2)
			relatedList.add(Q6_RELATED.get(5));
		else if(riskProfileTypeIdx == 0)
			relatedList.add(Q6_RELATED.get(6));
		
		List<JsonObject> result = makeVideoJsonObject(relatedList);
		
		return result;
	}
	
	private List<JsonObject> getInvestTypeRecommend(
			int investScore, 
			int investTracingScore,
			int riskProfileTypeIdx,
			int riskTracingTypeIdx,
			int riskAnsNum10)
	{
		List<String> relatedList = new ArrayList<>();
		
		// 자가진단 코멘트 및 추천 콘텐츠 정리 20210902 추천코멘트(고객작성) #행동편향 규칙 참고
		if(investScore < 85)
		{
			relatedList.add(Q5_RELATED.get(0));
			relatedList.add(Q5_RELATED.get(1));
			relatedList.add(Q5_RELATED.get(2));
			relatedList.add(Q5_RELATED.get(3));
		}
		
		if(riskProfileTypeIdx == 0 || (riskProfileTypeIdx == 1 && riskTracingTypeIdx == 0))
			relatedList.add(Q5_RELATED.get(4));
		else if((riskProfileTypeIdx == 1 && riskTracingTypeIdx == 2) || riskProfileTypeIdx == 2)
			relatedList.add(Q5_RELATED.get(5));
		
		if(riskAnsNum10 <= 2)
			relatedList.add(Q5_RELATED.get(6));		// 단기 투자자 관련 질문
		else 
			relatedList.add(Q5_RELATED.get(7));		// 장기 투자자 관련 질문
		
		if(investTracingScore <= 36)
		{
			relatedList.add(Q5_RELATED.get(8));
			relatedList.add(Q5_RELATED.get(9));
		}
		
		List<JsonObject> result = makeVideoJsonObject(relatedList);
		
		return result;
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
			Map<String, List<String>> recommendIdList,
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
					
					List<JsonObject> videoObjs = makeVideoJsonObject(
							recommendIdList.get(recommendIdsKey[keyValue]));
					
					result.addAll(videoObjs);
				}
			}
		}
		
		return result;
	}
	
	private JsonArray getBasicRecommend(List<Pair<Problem, Integer>> commonProbChoiceInfo) throws Exception
	{
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

		Map<String, List<String>> recommendIdList = new HashMap<>();		
		for(int keyIdx = 0; keyIdx < recommendIdsKey.length; keyIdx++)
		{
			List<String> input = new ArrayList<>();
			
			List<Integer> recIdIdxList = recoContentList.get(keyIdx);

			for(int idIdx : recIdIdxList)
				input.add(Q1_RELATED.get(idIdx));
			
			recommendIdList.put(recommendIdsKey[keyIdx], input);
		}
		
		List<JsonObject> resultBucket = getRecommend(commonProbChoiceInfo, recommendIdList, INVEST_BASIC_CURR_ID);
		
		return makeRandomResult(resultBucket);
	}
	
	private List<JsonObject> getTypeRecommend(
			List<Pair<Problem, Integer>> typeProbChoiceInfo) 
			throws Exception
	{
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

		Map<String, List<String>> recommendIdList = new HashMap<>();		
		for(int keyIdx = 0; keyIdx < recommendIdsKey.length; keyIdx++)
		{
			List<String> input = new ArrayList<>();
			
			List<Integer> recIdIdxList = recoContentList.get(keyIdx);

			for(int idIdx : recIdIdxList)
				input.add(Q2_RELATED.get(idIdx));
			
			recommendIdList.put(recommendIdsKey[keyIdx], input);
		}
		
		List<JsonObject> resultBucket = getRecommend(typeProbChoiceInfo, recommendIdList, CHOICE_STOCK_CURR_ID);
		
		return resultBucket;
	}
	
	private List<JsonObject> getChangeRecommend(
			List<Pair<Problem, Integer>> changeProbChoiceInfo) 
			throws Exception
	{
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

		Map<String, List<String>> recommendIdList = new HashMap<>();		
		for(int keyIdx = 0; keyIdx < recommendIdsKey.length; keyIdx++)
		{
			List<String> input = new ArrayList<>();
			
			List<Integer> recIdIdxList = recoContentList.get(keyIdx);

			for(int idIdx : recIdIdxList)
				input.add(Q3_RELATED.get(idIdx));
			
			recommendIdList.put(recommendIdsKey[keyIdx], input);
		}
		
		List<JsonObject> resultBucket = getRecommend(changeProbChoiceInfo, recommendIdList, PRICE_CHANGE_CURR_ID);
		
		return resultBucket;
	}
	
	private List<JsonObject> getSellRecommend(
			List<Pair<Problem, Integer>> sellProbChoiceInfo) 
			throws Exception
	{
		
		
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

		Map<String, List<String>> recommendIdList = new HashMap<>();		
		for(int keyIdx = 0; keyIdx < recommendIdsKey.length; keyIdx++)
		{
			List<String> input = new ArrayList<>();
			
			List<Integer> recIdIdxList = recoContentList.get(keyIdx);

			for(int idIdx : recIdIdxList)
				input.add(VOCA_RELATED.get(idIdx));
			
			recommendIdList.put(recommendIdsKey[keyIdx], input);
		}
		
		List<JsonObject> resultBucket = getRecommend(sellProbChoiceInfo, recommendIdList, SELL_WAY_CURR_ID);
		
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
	
	private JsonObject makeRecJsonObject(Video video)
	{
		JsonObject res = new JsonObject();
		res.addProperty("id", video.getVideoId());
		res.addProperty("type", video.getType());
		
		return res;
	}
	
	
	private List<JsonObject> makeVideoJsonObject(List<String> relatedList){
		List<JsonObject> result = new ArrayList<>();
		List<Video> videoRes = videoRepo.findAllByRelatedIn(relatedList);
		
		for(Video video : videoRes)
			result.add(makeRecJsonObject(video));
		
		return result;
	}
}
