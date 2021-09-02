package com.tmax.eTest.Report.dto;

// import java.util.ArrayList;
// import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.tmax.eTest.Report.dto.minitest.PartInfoDTO;
import com.tmax.eTest.Report.dto.minitest.PartDataDTO;

// import com.google.gson.JsonArray;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MiniTestRecordDTO {
	
	//score stats
	private int totalScore;
	private int totalPercentage;
	
	private String userName;
	//score info
	private PartInfoDTO partInfo;
	private Map<String, PartDataDTO> partInfoReadable;
	
	//problem stat info
	private Map<String, Object> problemCorrectInfo;
	private List<List<String>> problemHighLevelInfo;
	private List<List<String>> problemMiddleLevelInfo;
	private List<List<String>> problemLowLevelInfo;

	//Commentary
	private String totalComment;

	//alarm
	private boolean alarm;
}
