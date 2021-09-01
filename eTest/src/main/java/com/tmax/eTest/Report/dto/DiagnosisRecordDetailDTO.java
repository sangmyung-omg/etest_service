package com.tmax.eTest.Report.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tmax.eTest.Common.model.problem.Problem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosisRecordDetailDTO {

	int score = 0;
	int percentage = 0;
	
	// Comment 관련
	Map<String, String> mainCommentInfo = new HashMap<>();
	List<Object> detailCommentInfo = new ArrayList<>();
	
	// 문제 정보
	Map<String, Object> problemCorrectInfo = new HashMap<>();
	List<List<String>> problemHighLevelInfo = new ArrayList<>();
	List<List<String>> problemMiddleLevelInfo = new ArrayList<>();
	List<List<String>> problemLowLevelInfo = new ArrayList<>();

	public void initForDummy()
	{
		mainCommentInfo.put("main", "현명한 하이브리드형 투자자");
		mainCommentInfo.put("detail", "“하이브리드형 투자자”는 공격적일때는 공격적으로, "+
				"보수적일때는 보수적으로 상황에 맞게 투자방법을 결정하는 경향이 있습니다. "
				+ "대체로 내가 감당할 수 있는 범위 내에서 투자하는 것을 선호하며, "
				+ "나의 위험성향과 실제 투자 방식이 상당부분 일치하기 때문에 주변 사람들이 "
				+ "봤을때는 안정적으로 투자하는 사람으로 보이기도 합니다.");
		
		Map<String, Object> riskCommentInfo = new HashMap<>();
		Map<String, Object> investInfo = new HashMap<>();
		
		riskCommentInfo.put("name", "투자위험 태도");
		riskCommentInfo.put("main", "너무 큰 위험도 너무 작은 위험도 추구하지 않는, 중립형 투자자");
		riskCommentInfo.put("detail", "당신은 리스크 중립형으로 위험을 적당하게 수용하고 인내하는 투자자입니다.");
		riskCommentInfo.put("score", 40);
		
		investInfo.put("name", "투자 방법");
		investInfo.put("main", "집중할 때는 집중, 분산할 때는 분산! 중간형 투자자");
		investInfo.put("detail", "당신은 집중할때는 집중하고 분산할때는 분산하기도 하지만, "
				+ "남들과 비슷한 수준의 분산 투자를 하는 경향이 있습니다");
		investInfo.put("score", 42);
		
		detailCommentInfo.add(riskCommentInfo);
		detailCommentInfo.add(investInfo);
		
		problemCorrectInfo.put("allCorr", 3);
		problemCorrectInfo.put("allProb", 6);
		problemCorrectInfo.put("high", "1/2");
		problemCorrectInfo.put("middle", "1/2");
		problemCorrectInfo.put("low", "1/2");

		List<String> probInfo = new ArrayList<>();
		probInfo.add("문제 Index");
		probInfo.add("21");//문제 UUID
		probInfo.add("정오답 여부(true or false)");
		probInfo.add("문제 내용");
		probInfo.add("난이도");
		
		problemHighLevelInfo.add(probInfo);
		problemHighLevelInfo.add(probInfo);
		problemMiddleLevelInfo.add(probInfo);
		problemMiddleLevelInfo.add(probInfo);
		problemLowLevelInfo.add(probInfo);
		problemLowLevelInfo.add(probInfo);
	}
	
	private List<String> convertProblemToProbDTOInfo(Problem prob, int isCorr)
	{
		List<String> probInfoDTO = new ArrayList<>();
		
		probInfoDTO.add("문제 Index");
		probInfoDTO.add(prob.getProbID().toString());
		probInfoDTO.add(isCorr+"");
		probInfoDTO.add(prob.getQuestion());
		probInfoDTO.add(prob.getDifficulty());
		
		return probInfoDTO;
	}
	
	// 문제 정보 삽입 함수.
	public boolean pushProblemList(List<Problem> probs, Map<Integer, Integer> probCorrInfo)
	{
		// initialize
		problemCorrectInfo = new HashMap<>();
		problemHighLevelInfo = new ArrayList<>();
		problemMiddleLevelInfo = new ArrayList<>();
		problemLowLevelInfo = new ArrayList<>();
		
		//맞은 문제 난이도 상 중 하, 전체 문제 난이도 상 중 하
		
		int[] probCorrAndAllNum = {0,0,0,0,0,0};
		for(Problem prob : probs)
		{
			int isCorr = probCorrInfo.get(prob.getProbID());
			List<String> probDTO = convertProblemToProbDTOInfo(prob, isCorr);
			
			switch(prob.getDifficulty())
			{
			case "상":
				problemHighLevelInfo.add(probDTO);
				if(isCorr==1) probCorrAndAllNum[0]++;
				probCorrAndAllNum[3]++;
				break;
			case "중":
				problemMiddleLevelInfo.add(probDTO);
				if(isCorr==1) probCorrAndAllNum[1]++;
				probCorrAndAllNum[4]++;
				break;
			case "하":
				problemLowLevelInfo.add(probDTO);
				if(isCorr==1) probCorrAndAllNum[2]++;
				probCorrAndAllNum[5]++;
				break;
			default:
				break;
			}
		}
		
		int allCorr = probCorrAndAllNum[0] + probCorrAndAllNum[1] + probCorrAndAllNum[2];
		
		problemCorrectInfo.put("allCorr", allCorr);
		problemCorrectInfo.put("allProb", probs.size());
		problemCorrectInfo.put("high", probCorrAndAllNum[0]+"/"+probCorrAndAllNum[3]);
		problemCorrectInfo.put("middle", probCorrAndAllNum[1]+"/"+probCorrAndAllNum[4]);
		problemCorrectInfo.put("low", probCorrAndAllNum[2]+"/"+probCorrAndAllNum[5]);
		
		
		return true;
	}
	
	
	
}
