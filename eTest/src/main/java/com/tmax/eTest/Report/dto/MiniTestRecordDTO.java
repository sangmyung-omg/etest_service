package com.tmax.eTest.Report.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class MiniTestRecordDTO {
	
	// 최종 점수
	int totalScore=0, totalPercentage=0;
	
	String userName = "이름";

	// 점수 관련
	Map<String, Object> partInfo = new HashMap<>();
	
	// 문제 정보
	Map<String, Object> problemCorrectInfo = new HashMap<>();
	List<List<String>> problemHighLevelInfo = new ArrayList<>();
	List<List<String>> problemMiddleLevelInfo = new ArrayList<>();
	List<List<String>> problemLowLevelInfo = new ArrayList<>();
	
	public boolean initForDummy() {
		totalScore = 85;
		totalPercentage = 22;
		userName = "홍길동";
		
		Map<String, Object> part1Info = new HashMap<>();
		List<List<String>> part1UKInfo = new ArrayList<>();
		
		List<String> ukInfo = new ArrayList<String>();
		ukInfo.add("UK 이름");
		ukInfo.add("UK 점수");
		ukInfo.add("UK 설명");
		part1UKInfo.add(ukInfo);
		part1UKInfo.add(ukInfo);
		
		part1Info.put("score", 71);
		part1Info.put("percentage", 20);
		part1Info.put("ukInfo", part1UKInfo);

		partInfo.put("part1", part1Info);
		partInfo.put("part2", part1Info);
		partInfo.put("part3", part1Info);
		partInfo.put("part4", part1Info);
		partInfo.put("part5", part1Info);
		partInfo.put("part6", part1Info);
		
		problemCorrectInfo.put("allCorr", 3);
		problemCorrectInfo.put("allProb", 6);
		problemCorrectInfo.put("high", "1/2");
		problemCorrectInfo.put("middle", "1/2");
		problemCorrectInfo.put("low", "1/2");

		List<String> probInfo = new ArrayList<>();
		probInfo.add("문제 Index");
		probInfo.add("1");//문제 UUID
		probInfo.add("정오답 여부(true or false)");
		probInfo.add("문제 내용");
		probInfo.add("난이도");
		
		problemHighLevelInfo.add(probInfo);
		problemHighLevelInfo.add(probInfo);
		problemMiddleLevelInfo.add(probInfo);
		problemMiddleLevelInfo.add(probInfo);
		problemLowLevelInfo.add(probInfo);
		problemLowLevelInfo.add(probInfo);
		
		return true;
	}

}