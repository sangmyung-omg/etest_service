package com.tmax.eTest.Report.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Report.dto.MiniTestResultDTO;
import com.tmax.eTest.Report.dto.VideoResultDTO;
import com.tmax.eTest.Report.service.MiniTestScoreService;
import com.tmax.eTest.Report.service.MiniTestVideoService;
import com.tmax.eTest.Test.service.UserInfoService;

@RestController
public class MiniTestReportController {
	
	@Autowired
	MiniTestScoreService miniTestScoreService;
	@Autowired
	MiniTestVideoService miniTestVideoService;

	
	@Autowired
	UserInfoService userService;
	
	@CrossOrigin("*")
	@GetMapping(value="/report/miniTestResult/{id}", produces = "application/json; charset=utf-8")
	public MiniTestResultDTO miniTestResult(@PathVariable("id") String id) throws Exception{
		
		// Saving user ID of not-logged-in users - by S.M.
		//String updateResult = userService.updateUserInfo(id);
		// ------------------------------------------------
		
		MiniTestResultDTO output = miniTestScoreService.getMiniTestResult(id, null);

		return output;
	}
	

	@CrossOrigin("*")
	@GetMapping(value="/report/miniTestResult/{id}/{probSetId}", produces = "application/json; charset=utf-8")
	public MiniTestResultDTO miniTestResult(
			@PathVariable("id") String id,
			@PathVariable("probSetId") String probSetId) throws Exception{
		
		// Saving user ID of not-logged-in users - by S.M.
		//String updateResult = userService.updateUserInfo(id);
		// ------------------------------------------------
		
		MiniTestResultDTO output = miniTestScoreService.getMiniTestResult(id, probSetId);

		return output;
	}

	
	
	@CrossOrigin("*")
	@PutMapping(value="/report/miniTestResult/recommendVideoBookmark/{userId}/{videoId}/{isBookmarkOn}", produces = "application/json; charset=utf-8")
	public boolean setVideoBookmark(
			@PathVariable("userId") String userId,
			@PathVariable("videoId") Long videoId,
			@PathVariable("isBookmarkOn") Boolean isBookmarkOn) throws Exception{
		

		return miniTestVideoService.setVideoBookmark(userId, videoId, isBookmarkOn);
	}

	@CrossOrigin("*")
	@GetMapping(value="/report/miniTestResult/recommendVideo/{userId}", produces = "application/json; charset=utf-8")
	public List<VideoResultDTO> recommendVideo(
			@PathVariable("userId") String userId) throws Exception{
		
		return miniTestVideoService.getRecommendVideo(userId);
		
		
		
	}
	
}
