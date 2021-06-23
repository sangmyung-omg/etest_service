package com.tmax.eTest.Report.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Report.dto.DiagnosisResultDTO;
import com.tmax.eTest.Report.dto.PartUnderstandingDTO;
import com.tmax.eTest.Report.util.LRSAPIManager;

@RestController
public class SelfDiagnosisReportController {
	
	@GetMapping(value="/report/diagnosisResult/{id}", produces = "application/json; charset=utf-8")
	public DiagnosisResultDTO diagnosisResult(
			@PathVariable("id") String id) throws Exception{
		DiagnosisResultDTO output = new DiagnosisResultDTO();

		LRSAPIManager.getLRSUpdateProblemSequence(id);
		
		output.initForDummy();
		
		return output;
	}
	
	@GetMapping(value="/report/diagnosisResult/{id}/{part}", produces = "application/json; charset=utf-8")
	public PartUnderstandingDTO partUnderstanding(
			@PathVariable("id") String id,
			@PathVariable("part") String part) throws Exception{
		
		PartUnderstandingDTO output = new PartUnderstandingDTO();
		output.initForDummy();

		return output;
	}
}
