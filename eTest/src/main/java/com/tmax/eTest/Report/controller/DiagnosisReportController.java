package com.tmax.eTest.Report.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Report.dto.DiagnosisRecordDetailDTO;
import com.tmax.eTest.Report.dto.DiagnosisRecordMainDTO;
import com.tmax.eTest.Report.dto.PartUnderstandingDTO;
import com.tmax.eTest.Report.service.DiagnosisRecordService;
import com.tmax.eTest.Report.service.DiagnosisReportService;
import com.tmax.eTest.Test.service.UserInfoService;

@RestController
public class DiagnosisReportController {
	
	@Autowired
	DiagnosisReportService selfDiagnosisReportService;
	
	@Autowired
	DiagnosisRecordService diagnosisRecordService;
	
	@Autowired
	UserInfoService userService;
	

	@CrossOrigin("*")
	@PutMapping(value="/report/diagnosis/saveResult/{id}/{probSetId}", produces = "application/json; charset=utf-8")
	public boolean diagnosisResult(
			@PathVariable("id") String id,
			@PathVariable("probSetId") String probSetId) throws Exception{
		
		// Saving user ID of not-logged-in users - by S.M.
		//String queryResult = userService.updateUserInfo(id);
		// ------------------------------------------------
		
		boolean output = selfDiagnosisReportService.saveDiagnosisResult(id, probSetId);
		
		return output;
	}
	
	@CrossOrigin("*")
	@GetMapping(value="/report/diagnosis/record/{id}/{probSetId}", produces = "application/json; charset=utf-8")
	public DiagnosisRecordMainDTO diagnosisRecordMain(
			@PathVariable("id") String id,
			@PathVariable("probSetId") String probSetId) throws Exception{
		
		// Saving user ID of not-logged-in users - by S.M.
		//String queryResult = userService.updateUserInfo(id);
		// ------------------------------------------------
		
		DiagnosisRecordMainDTO output = diagnosisRecordService.getDiagnosisRecordMain(id, probSetId);
		
		return output;
	}
	
	@CrossOrigin("*")
	@GetMapping(value="/report/diagnosis/record/{id}/{probSetId}/{partName}", produces = "application/json; charset=utf-8")
	public DiagnosisRecordDetailDTO diagnosisRecordDetail(
			@PathVariable("id") String id,
			@PathVariable("probSetId") String probSetId,
			@PathVariable("partName") String partName) throws Exception{
		
		// Saving user ID of not-logged-in users - by S.M.
		//String queryResult = userService.updateUserInfo(id);
		// ------------------------------------------------
		
		DiagnosisRecordDetailDTO output = diagnosisRecordService.getDiagnosisRecordDetail(id, probSetId, partName);
		
		return output;
	}
	
	
	
	@CrossOrigin("*")
	@GetMapping(value="/report/diagnosisPart/{id}/{part}", produces = "application/json; charset=utf-8")
	public PartUnderstandingDTO partUnderstanding(
			@PathVariable("id") String id,
			@PathVariable("part") String part) throws Exception{
		
		PartUnderstandingDTO output = selfDiagnosisReportService.getPartInfo(id, part);

		return output;
	}
}