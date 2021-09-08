package com.tmax.eTest.Report.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tmax.eTest.Report.dto.triton.TritonDataDTO;

import lombok.extern.log4j.Log4j2;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.TestProblem;
import com.tmax.eTest.Common.model.uk.ProblemUKRelation;
import com.tmax.eTest.Common.model.uk.UkMaster;

@Component
@Log4j2
public class UKScoreCalculator {

	public String calculateUKScoreString(float ukScore)
	{
		if(ukScore >= 0.85f)
			return "A";
		else if(ukScore >= 0.7f)
			return "B";
		else if(ukScore >= 0.5f)
			return "C";
		else if(ukScore >= 0.3f)
			return "D";
		else if(ukScore >= 0.15f)
			return "E";
		else
			return "F";
	}
	
	
	
	public float makeAllThemeAvg(Map<String, List<List<String>>> themeInfo)
	{
		float result = 0;
		int count = 0;
		
		for (Map.Entry<String, List<List<String>>> entry : themeInfo.entrySet()) {
			float themeSum = 0;
			int themeCount = 0;
			for(List<String> ukInfo : entry.getValue())
			{
				try {
					String scoreStr = ukInfo.get(2);
					themeSum += Float.parseFloat(scoreStr);
					themeCount++;
				}
				catch(Exception e)
				{
					log.info("Error in makeAllThemeAvg. "+ ukInfo.toString());
				}
			}
			
			if(themeCount != 0)
			{
				result += themeSum / themeCount;
				count++;
			}
		}
		
		if(count != 0)
			result /= count;
		
		return result;
	}
	// example
	// {"주제 이름" : [["UKID", "Uk 이름", "UK 점수"], ["UKID", "Uk 이름", "UK 점수"], ["UKID", "Uk 이름", "UK 점수"], ...],
	// "주제 이름" : [["UKID", "Uk 이름", "UK 점수"], ["UKID", "Uk 이름", "UK 점수"], ["UKID", "Uk 이름", "UK 점수"], ...]}
	public Map<String, List<List<String>>> makeThemeInfo(
			Map<Integer, Float> ukScore,
			List<Problem> probInfos) 
	{
		Map<String, List<List<String>>> result = new HashMap<>();
		Map<String, List<String>> themeAndUkMap = new HashMap<>();
		
		for(Problem prob : probInfos)
		{
			TestProblem themeInfo = prob.getTestInfo();
			if(themeInfo != null)
			{
				String themeName = themeInfo.getPart().getPartName();
				List<ProblemUKRelation> probUkInfos = prob.getProblemUKReleations();
				
				for(ProblemUKRelation probUkInfo : probUkInfos)
				{
					List<String> partUK = new ArrayList<>();
					UkMaster ukInfo = probUkInfo.getUkId();
					String ukName = ukInfo.getUkName();
					
					if(themeAndUkMap.get(themeName) == null)
					{
						themeAndUkMap.put(themeName, new ArrayList<>());
					}
										
					if(!themeAndUkMap.get(themeName).contains(ukName))
					{
						partUK.add(ukInfo.getUkId()+"");
						partUK.add(ukInfo.getUkName());
						partUK.add(Float.toString(ukScore.get(ukInfo.getUkId()) * 100));
						
						if(result.get(themeName) == null)
						{
							List<List<String>> themeResult = new ArrayList<>();
							themeResult.add(partUK);
							result.put(themeName, themeResult);
						}
						else
						{
							result.get(themeName).add(partUK);
						}
						themeAndUkMap.get(themeName).add(ukName);
					}
				}
			}
		}
		
		return result;
	}
	

	public Map<String, List<List<String>>> makePartUkDetail(
			Map<Integer, UkMaster> ukMap, 
			Map<Integer, Float> ukScore) 
	{
		Map<String, List<List<String>>> result = new HashMap<>();

		// 파트 구불법 필요.
		ukScore.forEach((ukId, score) -> {
			UkMaster recentUK = ukMap.get(ukId);
			if (recentUK != null) {
				// example
				List<String> partUK = new ArrayList<>();

				partUK.add(ukId+"");
				partUK.add(recentUK.getUkName());
				partUK.add(Float.toString(ukScore.get(ukId) * 100));

				if(result.get(recentUK.getPart()) == null)
				{
					List<List<String>> partInfo = new ArrayList<>();
					partInfo.add(partUK);
					result.put(recentUK.getPart(), partInfo);
				}
				else
				{
					result.get(recentUK.getPart()).add(partUK);
				}
			}
		
		});
		
		return result;
	}

	
	public List<List<String>> makePartScore(Map<Integer, UkMaster> ukMap, Map<Integer, Float> ukScore) {
		List<List<String>> result = new ArrayList<>();
		Map<String, Pair<Float, Integer>> partInfo = new HashMap<>();

		ukScore.forEach((ukUuid, score) -> {
			UkMaster ukInfo = ukMap.get(ukUuid);
			if (ukInfo != null) {
				String partName = ukInfo.getPart();

				if (partInfo.get(partName) != null) {
					Pair<Float, Integer> scoreInfo = partInfo.get(partName);

					int partNum = scoreInfo.getSecond() + 1;
					float avg = (scoreInfo.getFirst() * scoreInfo.getSecond() + score) / (float) partNum;

					partInfo.put(partName, Pair.of(avg, partNum));

				} else { // ukInfo 자체가 파트일 경우.
					Pair<Float, Integer> scoreInfo = Pair.of(score, 1);
					partInfo.put(partName, scoreInfo);
				}
			}

		});

		partInfo.forEach((part, info) -> {
			List<String> partScore = new ArrayList<>();
			partScore.add(part);

			String scoreStr = calculateUKScoreString(info.getFirst());
			
			partScore.add(scoreStr);
			// 외부로 나가는 값엔 * 100
			partScore.add(String.valueOf(Math.round(info.getFirst()*100)));

			result.add(partScore);
		});

		return result;
	}

	public Map<Integer, Float> makeUKScoreMap(TritonDataDTO mastery) {
		Map<Integer, Float> result = new HashMap<Integer, Float>();

		JsonObject masteryJson = (JsonObject) JsonParser.parseString((String) mastery.getData().get(0));

		masteryJson.keySet().forEach(ukId -> {
			int ukUuid = Integer.parseInt(ukId);
			result.put(ukUuid, masteryJson.get(ukId).getAsFloat());
		});

		return result;
	}
}
