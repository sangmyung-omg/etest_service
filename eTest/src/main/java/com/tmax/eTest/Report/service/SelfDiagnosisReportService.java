package com.tmax.eTest.Report.service;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Report.dto.DiagnosisResultDTO;
import com.tmax.eTest.Report.dto.lrs.GetStatementInfoDTO;
import com.tmax.eTest.Report.dto.triton.TritonRequestDTO;
import com.tmax.eTest.Report.util.LRSAPIManager;
import com.tmax.eTest.Report.util.TritonAPIManager;

@Service
public class SelfDiagnosisReportService {

	@Autowired
	LRSAPIManager lrsAPIManager;
	
	@Autowired
	TritonAPIManager tritonAPIManager;
	
	public DiagnosisResultDTO calculateDiagnosisResult(String id)
	{
		DiagnosisResultDTO result = new DiagnosisResultDTO();
		
		GetStatementInfoDTO getStateInfo = new GetStatementInfoDTO();
		TritonRequestDTO getInferInfo = new TritonRequestDTO();
		getInferInfo.initForDummy();
		getStateInfo.pushUserId(id);
		
		try {
			lrsAPIManager.getStatementList(getStateInfo);
			tritonAPIManager.getInfer(getInferInfo);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		result.initForDummy();
		
		return result;
	}
	
}
