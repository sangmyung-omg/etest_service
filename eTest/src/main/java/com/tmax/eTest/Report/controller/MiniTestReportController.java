package com.tmax.eTest.Report.controller;


import javax.servlet.http.HttpServletRequest;

// import com.tmax.eTest.Auth.jwt.JwtTokenUtil;
// import com.tmax.eTest.LRS.util.JWTUtil;

// import java.util.List;

import com.tmax.eTest.Report.dto.MiniTestRecordDTO;
import com.tmax.eTest.Report.service.MiniTestRecordService;
import com.tmax.eTest.Report.service.MiniTestScoreService;

import com.tmax.eTest.Report.util.UserIdFetchTool;
import com.tmax.eTest.Test.service.UserInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

// import com.google.gson.Gson;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
public class MiniTestReportController {

	@Autowired
	MiniTestScoreService miniTestScoreService;
	@Autowired
	MiniTestRecordService miniTestRecordService;

	@Autowired
	UserInfoService userService;

	@Autowired private UserIdFetchTool userIdFetchTool;


	@CrossOrigin("*")
	@PutMapping(value = "/report/miniTest/saveResult/{id}/{probSetId}", produces = "application/json; charset=utf-8")
	public boolean saveMiniTestResult(@PathVariable("id") String id, @PathVariable("probSetId") String probSetId)
			throws Exception {

		// Saving user ID of not-logged-in users - by S.M.
		// String updateResult = userService.updateUserInfo(id);
		// ------------------------------------------------

		boolean output = miniTestScoreService.saveMiniTestResult(id, probSetId);

		return output;
	}

	@CrossOrigin("*")
	@GetMapping(value="/report/miniTest/record/{id}/{probSetId}", produces = "application/json; charset=utf-8")
	public ResponseEntity<?> miniTestRecord(
			@PathVariable("id") String id,
			@PathVariable("probSetId") String probSetId) throws Exception{
		
		MiniTestRecordDTO output = miniTestRecordService.getMiniTestRecord(id, probSetId);

		return ResponseEntity.ok().body(output);
	}

	@CrossOrigin("*")
	@PutMapping(value="/report/miniTest/saveAndGetResult/{id}/{probSetId}", produces = "application/json; charset=utf-8")
	public ResponseEntity<?> saveAndGetMiniTestRecord(
			@PathVariable("id") String id,
			@PathVariable("probSetId") String probSetId) throws Exception{
		
		miniTestScoreService.saveMiniTestResult(id, probSetId);
		MiniTestRecordDTO output = miniTestRecordService.getMiniTestRecord(id, probSetId);

		return ResponseEntity.ok().body(output);
	}
	//Secure API 
	
	@CrossOrigin("*")
	@DeleteMapping(value = "/report/minitest", produces = "application/json; charset=utf-8")
	public ResponseEntity<?> deleteMiniTestResult(HttpServletRequest request, 
			@RequestParam("probSetId") String probSetId) throws Exception {
		//Extract id from auth
		String id = userIdFetchTool.getID(request);
		boolean result = false;

		if(id == null){
			return ResponseEntity.internalServerError().body("Cannot get uuid from token info");
		}
		
		result = miniTestScoreService.deleteMiniTestResult(id, probSetId);

		
		return ResponseEntity.ok().body(result);
	}
	

	@CrossOrigin("*")
	@PutMapping(value = "/report/minitest", produces = "application/json; charset=utf-8")
	public ResponseEntity<?> updateMiniTestResult(HttpServletRequest request, @RequestParam("probSetId") String probSetId, 
												 @RequestParam(value = "forceUpdate", defaultValue = "false") boolean forceUpdate) throws Exception {
		//Extract id from auth
		String id = userIdFetchTool.getID(request);

		if(id == null){
			return ResponseEntity.internalServerError().body("Cannot get uuid from token info");
		}
		
		//update if record does not exist or forceupdate
		if(!miniTestRecordService.checkMinireportExist(id, probSetId) || forceUpdate)
			miniTestScoreService.saveMiniTestResult(id, probSetId);

		MiniTestRecordDTO output = miniTestRecordService.getMiniTestRecord(id, probSetId);

		if(output == null)
			return ResponseEntity.internalServerError().body("Record not found");
		
		return ResponseEntity.ok().body(output);
	}


	@CrossOrigin("*")
	@GetMapping(value="/report/minitest", produces = "application/json; charset=utf-8")
	public ResponseEntity<?> readMiniTestRecord(HttpServletRequest request, @RequestParam("probSetId") String probSetId) throws Exception{
		//Extract id from auth
		String id = userIdFetchTool.getID(request);

		if(id == null){
			return ResponseEntity.internalServerError().body("Cannot get uuid from token info");
		}
		
		MiniTestRecordDTO output = miniTestRecordService.getMiniTestRecord(id, probSetId);
		if(output == null)
			return ResponseEntity.internalServerError().body("Record not found");

		return ResponseEntity.ok().body(output);
	}

}
