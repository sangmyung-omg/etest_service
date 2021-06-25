package com.tmax.eTest.Report.dto.lrs;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GetStatementInfoDTO {
	public List<String> actionTypeList;
	public String dateFrom;
	public String dateTo;
	public Integer recentStatementNum;
	public List<String> sourceTypeList;
	public List<String> userIdList;
	
	public boolean pushUserId(String id)
	{
		if(userIdList == null)
			userIdList = new ArrayList<String>();
		
		return userIdList.add(id);
	}
}
