package com.tmax.eTest.Report.controller;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Auth.jwt.JwtTokenUtil;
import com.tmax.eTest.Report.dto.DiagnosisRecordDetailDTO;
import com.tmax.eTest.Report.dto.DiagnosisRecordMainDTO;
import com.tmax.eTest.Report.service.DiagnosisDetailRecordService;
import com.tmax.eTest.Report.service.DiagnosisMainRecordService;
import com.tmax.eTest.Report.service.DiagnosisReportService;
import com.tmax.eTest.Report.service.DiagnosisVideoService;
import com.tmax.eTest.Test.service.UserInfoService;

import lombok.extern.log4j.Log4j2;

@Log4j2
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
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	

	@PutMapping(value="/saveResult/{probSetId}", produces = "application/json; charset=utf-8")
	public boolean diagnosisResult(
			HttpServletRequest request,
			@PathVariable("probSetId") String probSetId) throws Exception{
		
		String id = getUserIDFromRequest(request);
		
		boolean output = selfDiagnosisReportService.saveDiagnosisResult(id, probSetId);
		
		return output;
	}
	
	@GetMapping(value="/record/{probSetId}", produces = "application/json; charset=utf-8")
	public DiagnosisRecordMainDTO diagnosisRecordMain(
			HttpServletRequest request,
			@PathVariable("probSetId") String probSetId) throws Exception{
		

		String id = getUserIDFromRequest(request);
		
		DiagnosisRecordMainDTO output = diagnosisMainRecordService.getDiagnosisRecordMain(id, probSetId);
		
		return output;
	}
	
	@GetMapping(value="/record/{probSetId}/{partName}", produces = "application/json; charset=utf-8")
	public DiagnosisRecordDetailDTO diagnosisRecordDetail(
			HttpServletRequest request,
			@PathVariable("probSetId") String probSetId,
			@PathVariable("partName") String partName) throws Exception{
		
		String id = getUserIDFromRequest(request);
		
		DiagnosisRecordDetailDTO output = diagnosisDetailRecordService.getDiagnosisRecordDetail(id, probSetId, partName);
		
		return output;
	}
	
	@PutMapping(value="/bookmark/{videoId}/{isBookmarkOn}", produces = "application/json; charset=utf-8")
	public boolean setVideoBookmark(
			HttpServletRequest request,
			@PathVariable("videoId") Long videoId,
			@PathVariable("isBookmarkOn") Boolean isBookmarkOn) throws Exception{

		String id = getUserIDFromRequest(request);
		
		return diagnosisVideoService.setVideoBookmark(id, videoId, isBookmarkOn);
	}
	
	
	public String getUserIDFromRequest(HttpServletRequest request)
	{
		String result = null;
		String header = request.getHeader("Authorization");
		
		if (header == null) {																// 비회원일 때 토큰 null
			log.info("header.Authorization is null. No token given.");
		} else {																			// 회원이면 토큰 파싱하여 유저 아이디 꺼냄
			String token = request.getHeader("Authorization").replace("Bearer ","");
			try {
				Map<String, Object> parseInfo = jwtTokenUtil.getUserParseInfo(token);
				result = parseInfo.get("userUuid").toString();
				log.info("User UUID from header token : " + result);
			} catch (Exception e) {
				log.info("error : cannot parse jwt token, " + e.getMessage());
			}
		}
		
		return result;
	}
}
