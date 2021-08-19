package com.tmax.eTest.Report.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Report.dto.DiagnosisResultDTO;
import com.tmax.eTest.Report.dto.PartUnderstandingDTO;
import com.tmax.eTest.Report.service.SelfDiagnosisReportService;
import com.tmax.eTest.Report.util.LRSAPIManager;
import com.tmax.eTest.Test.service.UserInfoService;

@RestController
public class SelfDiagnosisReportController {
	
	@Autowired
	SelfDiagnosisReportService selfDiagnosisReportService;
	
	@Autowired
	UserInfoService userService;
	
	@CrossOrigin("*")
	@GetMapping(value="/report/diagnosisResult/{id}", produces = "application/json; charset=utf-8")
	public DiagnosisResultDTO diagnosisResult(
			@PathVariable("id") String id) throws Exception{
		
		// Saving user ID of not-logged-in users - by S.M.
		//String queryResult = userService.updateUserInfo(id);
		// ------------------------------------------------
		
		DiagnosisResultDTO output = selfDiagnosisReportService.calculateDiagnosisResult(id, null);
		
		return output;
	}
	
	@CrossOrigin("*")
	@GetMapping(value="/report/diagnosisResult/{id}/{probSetId}", produces = "application/json; charset=utf-8")
	public DiagnosisResultDTO diagnosisResult(
			@PathVariable("id") String id,
			@PathVariable("id") String probSetId) throws Exception{
		
		// Saving user ID of not-logged-in users - by S.M.
		//String queryResult = userService.updateUserInfo(id);
		// ------------------------------------------------
		
		DiagnosisResultDTO output = selfDiagnosisReportService.calculateDiagnosisResult(id, probSetId);
		
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
