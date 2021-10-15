package com.tmax.eTest.Report.controller;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Auth.jwt.JwtTokenUtil;
import com.tmax.eTest.Report.dto.DiagnosisRecordDetailDTO;
import com.tmax.eTest.Report.dto.DiagnosisRecordMainDTO;
import com.tmax.eTest.Report.exception.ReportBadRequestException;
import com.tmax.eTest.Report.service.DiagnosisDetailRecordService;
import com.tmax.eTest.Report.service.DiagnosisMainRecordService;
import com.tmax.eTest.Report.service.DiagnosisReportService;
import com.tmax.eTest.Report.service.DiagnosisVideoService;
import com.tmax.eTest.Test.service.UserInfoService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
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
	

	@Deprecated
	@GetMapping(value="/saveResult/{probSetId}", produces = "application/json; charset=utf-8")
	public boolean diagnosisResult(
			HttpServletRequest request,
			@PathVariable("probSetId") String probSetId) throws Exception{
		String id = getUserIDFromRequest(request);
		
		boolean output = selfDiagnosisReportService.saveDiagnosisResult(id, probSetId);
//		selfDiagnosisReportService.test();
		return true;
	}
	
	@DeleteMapping(value="/record", produces = "application/json; charset=utf-8")
	public ResponseEntity<?> deleteDiagnosisReport(
			HttpServletRequest request,
			@RequestParam("probSetId") String probSetId) throws Exception{

		String id = getUserIDFromRequest(request);
		
		if(id == null)
			throw new ReportBadRequestException("userId is null in deleteDiagnosisReport.");
		
		boolean result = selfDiagnosisReportService.deleteDiagnosisReport(id, probSetId);
		
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping(value="/record/{probSetId}", produces = "application/json; charset=utf-8")
	public DiagnosisRecordMainDTO diagnosisRecordMain(
			HttpServletRequest request,
			@PathVariable("probSetId") String probSetId) throws Exception{
		
		String id = getUserIDFromRequest(request);
		
		selfDiagnosisReportService.saveDiagnosisResult(id, probSetId);
		
		DiagnosisRecordMainDTO output = diagnosisMainRecordService.getDiagnosisRecordMain(id, probSetId);
		
		return output;
	}
	
	@GetMapping(value="/record/{probSetId}/{partName}", produces = "application/json; charset=utf-8")
	public DiagnosisRecordDetailDTO diagnosisRecordDetail(
			HttpServletRequest request,
			@PathVariable("probSetId") String probSetId,
			@PathVariable("partName") String partName) throws Exception{
		
		String userId = getUserIDFromRequest(request);
		
		DiagnosisRecordDetailDTO output = diagnosisDetailRecordService.getDiagnosisRecordDetail(userId, probSetId, partName);
		
		return output;
	}
	
	@PutMapping(value="/bookmark/{videoId}/{isBookmarkOn}", produces = "application/json; charset=utf-8")
	public boolean setVideoBookmark(
			HttpServletRequest request,
			@PathVariable("videoId") String videoId,
			@PathVariable("isBookmarkOn") Boolean isBookmarkOn) throws Exception{

		String id = getUserIDFromRequest(request);
		
		if(id == null)
			throw new ReportBadRequestException("userId is null in setVideoBookmark");
		
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
				
			} catch (UnsupportedJwtException e) {
				log.info("error : cannot parse jwt token, " + e.getMessage());
			} catch (MalformedJwtException  e) {
				log.info("error : cannot parse jwt token, " + e.getMessage());
			} catch (SignatureException  e) {
				log.info("error : cannot parse jwt token, " + e.getMessage());
			} catch (ExpiredJwtException  e) {
				log.info("error : cannot parse jwt token, " + e.getMessage());
			} catch (IllegalArgumentException  e) {
				log.info("error : cannot parse jwt token, " + e.getMessage());
			}
		}
		
		return result;
	}
}
