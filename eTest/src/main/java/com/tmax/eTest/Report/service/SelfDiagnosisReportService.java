package com.tmax.eTest.Report.service;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Report.dto.DiagnosisResultDTO;
import com.tmax.eTest.Report.dto.lrs.GetStatementInfoDTO;
import com.tmax.eTest.Report.util.LRSAPIManager;

@Service
public class SelfDiagnosisReportService {

	@Autowired
	LRSAPIManager lrsAPIManager;
	
	public DiagnosisResultDTO calculateDiagnosisResult(String id)
	{
		DiagnosisResultDTO result = new DiagnosisResultDTO();
		
		GetStatementInfoDTO getStateInfo = new GetStatementInfoDTO();
		getStateInfo.pushUserId(id);
		
		try {
			lrsAPIManager.getStatementList(getStateInfo);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		result.initForDummy();
		
		return result;
	}
	
}
