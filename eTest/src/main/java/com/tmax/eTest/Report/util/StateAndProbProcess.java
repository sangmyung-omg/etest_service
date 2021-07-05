package com.tmax.eTest.Report.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import com.tmax.eTest.Contents.model.Problem;
import com.tmax.eTest.Contents.model.ProblemUKRelation;
import com.tmax.eTest.Report.dto.lrs.StatementDTO;
import com.tmax.eTest.Test.model.UkMaster;

@Component
// Statement(LRS 정보) 와 Problem 정보가 필요한 단순 작업 관련 Method들 집합.
public class StateAndProbProcess {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	public final static String UK_LIST_KEY = "ukList";			// List<String>
	public final static String IS_CORRECT_LIST_KEY = "isCorrectList";// List<String>
	public final static String DIFF_LIST_KEY = "diffcultyList";	// List<String>


	public int[] calculateDiagQuestionInfo(List<StatementDTO> miniTestResult)
	{
		int diagQuestionInfo[] = {0,0,0};
		
		for(StatementDTO state : miniTestResult)
		{
			if(state.getIsCorrect() == 1)
				diagQuestionInfo[0]++;
			else if(state.getUserAnswer().equalsIgnoreCase("pass"))
				diagQuestionInfo[2]++;
			else
				diagQuestionInfo[1]++;
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
				int ukId = Integer.parseInt(probUKRel.getUkId().getUkId());
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
					int ukId = Integer.parseInt(probUKRel.getUkId().getUkId());
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
}
