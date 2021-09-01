package com.tmax.eTest.Report.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiagnosisRecommendDTO {
	
	@Data
	@NoArgsConstructor
	public class RecommendInfoDTO{
		String id;
		String type;
	}
	
	/*
	 * example
	 * 
	 * { 
	 * 		basic : [{id:"1", type:"video"}, ....... ],
	 * 		advanced : [{id:"1", type:"video"}, ....... ],
	 * 		type : []
	 * 
	 * }
	 * 
	 * */
	List<RecommendInfoDTO> basic = new ArrayList<>();
	List<RecommendInfoDTO> advanced = new ArrayList<>();
	List<RecommendInfoDTO> type = new ArrayList<>();
	
}
