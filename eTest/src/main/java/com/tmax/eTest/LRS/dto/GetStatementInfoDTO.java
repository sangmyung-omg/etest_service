package com.tmax.eTest.LRS.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetStatementInfoDTO {
	public List<String> actionTypeList;
	public String dateFrom;
	public String dateTo;
	public Integer recentStatementNum;
	public List<String> sourceTypeList;
	public List<String> userIdList;
	public List<String> containExtension;

	public boolean pushUserId(String id) {
		if (userIdList == null)
			userIdList = new ArrayList<String>();

		return userIdList.add(id);
	}

	public boolean pushSourceType(String sourceType) {
		if (sourceTypeList == null)
			sourceTypeList = new ArrayList<>();

		return sourceTypeList.add(sourceType);
	}

	public boolean pushActionType(String actionType) {
		if (actionTypeList == null)
			actionTypeList = new ArrayList<>();

		return actionTypeList.add(actionType);
	}

	public boolean pushExtensionStr(String str) {
		if (containExtension == null)
			containExtension = new ArrayList<>();

		return containExtension.add(str);
	}
}
