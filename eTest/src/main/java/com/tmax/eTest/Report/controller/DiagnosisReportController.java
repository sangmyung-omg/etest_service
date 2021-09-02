package com.tmax.eTest.Report.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Report.dto.DiagnosisRecordDetailDTO;
import com.tmax.eTest.Report.dto.DiagnosisRecordMainDTO;
import com.tmax.eTest.Report.service.DiagnosisDetailRecordService;
import com.tmax.eTest.Report.service.DiagnosisMainRecordService;
import com.tmax.eTest.Report.service.DiagnosisReportService;
import com.tmax.eTest.Report.service.DiagnosisVideoService;
import com.tmax.eTest.Test.service.UserInfoService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/report/diagnosis")
public class DiagnosisReportController {
	
	@Autowired
	DiagnosisReportService selfDiagnosisReportService;
	
	@Autowired
	DiagnosisMainRecordService diagnosisMainRecordService;
	@Autowired
	DiagnosisDetailRecordService diagnosisDetailRecordService;
	
	
	@Autowired
	DiagnosisVideoService diagnosisVideoService;
	
	@Autowired
	UserInfoService userService;
	

	@PutMapping(value="/saveResult/{id}/{probSetId}", produces = "application/json; charset=utf-8")
	public boolean diagnosisResult(
			@PathVariable("id") String id,
			@PathVariable("probSetId") String probSetId) throws Exception{
		
		// Saving user ID of not-logged-in users - by S.M.
		//String queryResult = userService.updateUserInfo(id);
		// ------------------------------------------------
		
		boolean output = selfDiagnosisReportService.saveDiagnosisResult(id, probSetId);
		
		return output;
	}
	
	@GetMapping(value="/record/{id}/{probSetId}", produces = "application/json; charset=utf-8")
	public DiagnosisRecordMainDTO diagnosisRecordMain(
			@PathVariable("id") String id,
			@PathVariable("probSetId") String probSetId) throws Exception{
		
		// Saving user ID of not-logged-in users - by S.M.
		//String queryResult = userService.updateUserInfo(id);
		// ------------------------------------------------
		
		DiagnosisRecordMainDTO output = diagnosisMainRecordService.getDiagnosisRecordMain(id, probSetId);
		
		return output;
	}
	
	@GetMapping(value="/record/{id}/{probSetId}/{partName}", produces = "application/json; charset=utf-8")
	public DiagnosisRecordDetailDTO diagnosisRecordDetail(
			@PathVariable("id") String id,
			@PathVariable("probSetId") String probSetId,
			@PathVariable("partName") String partName) throws Exception{
		
		// Saving user ID of not-logged-in users - by S.M.
		//String queryResult = userService.updateUserInfo(id);
		// ------------------------------------------------
		
		DiagnosisRecordDetailDTO output = diagnosisDetailRecordService.getDiagnosisRecordDetail(id, probSetId, partName);
		
		return output;
	}
	
	@PutMapping(value="/bookmark/{id}/{videoId}/{isBookmarkOn}", produces = "application/json; charset=utf-8")
	public boolean setVideoBookmark(
			@PathVariable("id") String userId,
			@PathVariable("videoId") Long videoId,
			@PathVariable("isBookmarkOn") Boolean isBookmarkOn) throws Exception{
		
		return diagnosisVideoService.setVideoBookmark(userId, videoId, isBookmarkOn);
	}
}
