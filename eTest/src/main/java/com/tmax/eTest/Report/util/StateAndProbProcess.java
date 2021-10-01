package com.tmax.eTest.Report.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.Common.repository.problem.ProblemRepo;
import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Report.exception.ReportBadRequestException;

import lombok.extern.log4j.Log4j2;

import com.tmax.eTest.Common.model.uk.ProblemUKRelation;
@Component
@Log4j2
// Statement(LRS 정보) 와 Problem 정보가 필요한 단순 작업 관련 Method들 집합.
public class StateAndProbProcess {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	public final static String UK_LIST_KEY = "ukList";			// List<String>
	public final static String IS_CORRECT_LIST_KEY = "isCorrectList";// List<String>
	public final static String DIFF_LIST_KEY = "diffcultyList";	// List<String>
	
	@Autowired
	ProblemRepo problemRepo;
	
	@Autowired
	LRSAPIManager lrsAPIManager;

	public List<List<String>> calculateDiagQuestionInfo(List<StatementDTO> miniTestResult)
	{
		List<List<String>> diagQuestionInfo = new ArrayList<>();
		
		diagQuestionInfo.add(new ArrayList<>());
		diagQuestionInfo.add(new ArrayList<>());
		diagQuestionInfo.add(new ArrayList<>());
		
		for(StatementDTO state : miniTestResult)
		{
			String probId = state.getSourceId();
			if(state.getIsCorrect() == 1)
				diagQuestionInfo.get(0).add(probId);
			else if(state.getUserAnswer().equalsIgnoreCase("pass"))
				diagQuestionInfo.get(2).add(probId);
			else
				diagQuestionInfo.get(1).add(probId);
		}
		
		return diagQuestionInfo;
	}
	
	
	public Map<Integer, UkMaster> makeUsedUkMap(List<Problem> probList)
	{
		Map<Integer, UkMaster> res = new HashMap<>();
		
		for(Problem prob : probList)
		{
			List<ProblemUKRelation> probUKRels = prob.getProblemUKReleations();
			
			for(ProblemUKRelation probUKRel : probUKRels)
			{
				int ukId = probUKRel.getUkId().getUkId();
				res.put(ukId, probUKRel.getUkId());
			}
		}
		
		return res;
		
	}
	
	
	public Map<String, List<Object>> makeInfoForTriton(
			List<StatementDTO> statementList,
			List<Problem> probList)
	{
		Map<String, List<Object>> result = new HashMap<>();
		
		List<Object> ukList = new ArrayList<>();
		List<Object> isCorrectList = new ArrayList<>();
		List<Object> diffcultyList = new ArrayList<>();
		
		// first process : 문제별 PK 얻어오기.
		Map<Integer, Integer> isCorrectMap = new HashMap<>();
		for(StatementDTO dto : statementList)
		{
			try
			{
				int probId = Integer.parseInt(dto.getSourceId());
				isCorrectMap.put(probId, dto.getIsCorrect());
			}
			catch(Exception e)
			{
				logger.info("makeInfoForTriton : "+e.toString()+" id : "+dto.getSourceId()+" error!");
			}
		}
		
		for(Problem prob : probList)
		{
			if(prob.getDifficulty() != null)
			{
				List<ProblemUKRelation> probUKRels = prob.getProblemUKReleations();
				int diff = 1;
				int isCorrect = isCorrectMap.get(prob.getProbID());
							
				switch(prob.getDifficulty())
				{
				case "상":
					diff = 1;
					break;
				case "중":
					diff = 2;
					break;
				case "하":
					diff = 3;
					break;
				default:
					break;
				}
				
				for(ProblemUKRelation probUKRel : probUKRels)
				{
					int ukId = probUKRel.getUkId().getUkId();
					
					ukList.add(ukId);
					isCorrectList.add(isCorrect);
					diffcultyList.add(diff);
				}
			}
		}
				
		result.put(UK_LIST_KEY, ukList);
		result.put(IS_CORRECT_LIST_KEY, isCorrectList);
		result.put(DIFF_LIST_KEY, diffcultyList);
		
		return result;
	}
	
	public List<StatementDTO> getDiagnosisKnowledgeProbInfo(String probSetId) 
			throws Exception {

		GetStatementInfoDTO input = new GetStatementInfoDTO();

		if (probSetId != null)
			input.pushExtensionStr(probSetId);
		input.pushActionType("submit");
		input.pushSourceType("diagnosis");
		
		List<StatementDTO> result = new ArrayList<>();
		List<StatementDTO> stateResult;
		Map<String, Integer> isIDExist = new HashMap<>();
		
		try
		{
			stateResult = lrsAPIManager.getStatementList(input);
		}
		catch(Exception e)
		{
			throw new ReportBadRequestException("Exception in Diagnosis Report, get statement part.", e);
		}
		

		for(StatementDTO state : stateResult)
		{
			String sourceID = state.getSourceId();
			
			if(isIDExist.get(sourceID) != null)
			{
				StatementDTO beforeState = result.get(isIDExist.get(sourceID));
				Timestamp beforeTimestamp = beforeState.getStatementDate();
				Timestamp recentTimestamp = state.getStatementDate();
				
				// 최신 것을 기준으로.
				if(beforeTimestamp.compareTo(recentTimestamp) < 0)
				{
					result.set(isIDExist.get(sourceID), state);
				}
			}
			else
			{
				result.add(state);
				isIDExist.put(sourceID, result.size()-1);
			}
		}
		
		log.info(result.size()+"    "+result.toString());
		

		return result;

	}
	
	// LRS에서 푼 문제 정보를 모아, 관련 정보 생성. (난이도별 문제 맞은 갯수, 해당 문제 정보 등)
	public Map<String, Object> getProbInfoInRecordDTO(List<StatementDTO> infos) throws Exception
	{

		Map<String, Object> result = new HashMap<>();
		List<List<String>> probHighLevelInfo = new ArrayList<>();
		List<List<String>> probMidLevelInfo = new ArrayList<>();
		List<List<String>> probLowLevelInfo = new ArrayList<>();
		Map<String, Object> problemCorrectInfo = new HashMap<>();
		
		// probId, isCorr
		Map<Integer, Integer> probCorrInfo = new HashMap<>();

		for (StatementDTO info : infos) {
			if (info.getSourceType().equals("diagnosis")) {
				try {
					int probId = Integer.parseInt(info.getSourceId());
					probCorrInfo.put(probId, info.getIsCorrect());
				} catch (Exception e) {
					log.info("Integer decode fail in setProbInfoInRecordDTO " + info.getSourceId());
				}
			}
		}

		List<Problem> diagProbs = problemRepo.findAllById(probCorrInfo.keySet());
		int[] diffCorr = {0, 0, 0};
		int[] diffAll = {0, 0, 0};
		int problemIdx = 1;
		
		for(Problem prob : diagProbs)
		{
			String[] diffList = {"상", "중", "하"};
			int diffIdx = -1;
			int isCorr = probCorrInfo.get(prob.getProbID());
			
			for(int i = 0; i < diffList.length; i ++)
				if(prob.getDifficulty().equals(diffList[i]))
					diffIdx = i;
			
			if(diffIdx < 0 || diffIdx >= diffAll.length)
			{
				log.error("Prob Diffcult error in setProbInfoInRecordDTO. probID : "
						+prob.getProbID()+" "+prob.getDifficulty());
				continue;
			}
			
			if(isCorr == 1)
				diffCorr[diffIdx]++;
			diffAll[diffIdx]++;
			
			List<String> probInfo = new ArrayList<>();
			
			String probContent = prob.getQuestion();
			
			probContent.replaceAll("\\\"", "\"");
			
			try {
				JsonArray jsonArr = JsonParser.parseString(probContent).getAsJsonArray();
				
				for(int i = 0; i < jsonArr.size(); i++)
				{
					JsonObject obj = jsonArr.get(i).getAsJsonObject();
					if(obj.get("type").getAsString().equals("QUESTION_TEXT"))
					{
						probContent = obj.get("data").getAsString();
						break;
					}
				}
			}
			catch(Exception e)
			{
				log.info("Json parse fail in setProbInfoInRecordDTO. "+probContent +" "+e.toString());
			}
					
			
			probInfo.add(String.valueOf(problemIdx));
			probInfo.add(String.valueOf(prob.getProbID()));
			probInfo.add((isCorr==1)?"true":"false");
			probInfo.add(probContent);
			probInfo.add(prob.getDifficulty());
			probInfo.add(prob.getIntention());
			
			switch(diffIdx)
			{
			case 0:
				probHighLevelInfo.add(probInfo);
				break;
			case 1:
				probMidLevelInfo.add(probInfo);
				break;
			case 2:
				probLowLevelInfo.add(probInfo);
				break;
			default:
				log.error("Prob Diffcult error in setProbInfoInRecordDTO. probID : "
						+prob.getProbID()+" "+prob.getDifficulty());
				break;
			}
			
			problemIdx++;
		}
		
		problemCorrectInfo.put("allCorr", diffCorr[0]+diffCorr[1]+diffCorr[2]);
		problemCorrectInfo.put("allProb", diffAll[0]+diffAll[1]+diffAll[2]);
		problemCorrectInfo.put("high", diffCorr[0]+"/"+diffAll[0]);
		problemCorrectInfo.put("middle", diffCorr[1]+"/"+diffAll[1]);
		problemCorrectInfo.put("low", diffCorr[2]+"/"+diffAll[2]);
		
		result.put("problemCorrectInfo", problemCorrectInfo);
		result.put("problemHighLevelInfo", probHighLevelInfo);
		result.put("problemMiddleLevelInfo", probMidLevelInfo);
		result.put("problemLowLevelInfo", probLowLevelInfo);
		
		return result;
		
	}
}
