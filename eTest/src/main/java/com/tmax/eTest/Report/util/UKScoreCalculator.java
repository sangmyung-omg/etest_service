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
import com.tmax.eTest.Test.model.UkMaster;

@Component
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

	public Map<String, List<List<String>>> makePartUkDetail(
			Map<Integer, UkMaster> ukMap, 
			Map<Integer, Float> ukScore, 
			List<List<String>> partScore) 
	{
		Map<String, List<List<String>>> result = new HashMap<>();

		// 파트 구불법 필요.
		ukScore.forEach((ukId, score) -> {
			UkMaster recentUK = ukMap.get(ukId);
			if (recentUK != null) {
				// example
				List<String> partUK = new ArrayList<>();

				partUK.add(recentUK.getUkName());
				//partUK.add(ukScore.get(ukId).toString());
				partUK.add(calculateUKScoreString(ukScore.get(ukId)));
				partUK.add("C");

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

	// partScore example ["partname", "C", "50"]
	public List<List<String>> makeWeakPartDetail(
			Map<Integer, UkMaster> ukMap, 
			Map<Integer, Float> ukScore, 
			List<List<String>> partScore) {
		
		List<List<String>> result = new ArrayList<>();
		Pair<String, Integer> lowestScorePart = Pair.of("NULL", 100);

		// check lowest score
		for (List<String> part : partScore) {
			String partName = part.get(0);
			int score = Integer.parseInt(part.get(2));

			if (lowestScorePart.getSecond() > score)
				lowestScorePart = Pair.of(partName, score);
		}

		// 파트 구불법 필요.
		String lowScorePartName = lowestScorePart.getFirst();
		ukScore.forEach((ukId, score) -> {
			UkMaster recentUK = ukMap.get(ukId);
			if (recentUK != null 
				&& lowScorePartName.equals(recentUK.getPart())) {
				// example
				List<String> partUK = new ArrayList<>();

				partUK.add(recentUK.getUkName());
				//partUK.add(ukScore.get(ukId).toString());
				partUK.add(calculateUKScoreString(ukScore.get(ukId)));
				partUK.add("C");

				result.add(partUK);
			}

		});

		return result;
	}

	
	//[파트이름, 스코어 등급(A~F), 스코어 점수]
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

				} else {
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
