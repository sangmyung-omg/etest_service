package com.tmax.eTest.Test.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Auth.jwt.JwtTokenUtil;
import com.tmax.eTest.Test.config.TestPathConstant;
import com.tmax.eTest.Test.dto.RegisterInputDTO;
import com.tmax.eTest.Test.service.UserInfoService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = TestPathConstant.apiPrefix)
public class UserInfoController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	@Autowired
	UserInfoService userInfoService;

	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@PutMapping(value = "/userInfo/{id}")
	public ResponseEntity<Object> putUserInfo(@PathVariable(value="id") String userId) throws Exception {
		Map<String, Object> ret = new HashMap<String, Object>();
		try{
			String res = userInfoService.updateUserInfo(userId);
			ret.put("resultMessage", res);
			return new ResponseEntity<>(ret, HttpStatus.OK);

		} catch (Exception E){
			ret.put("resultMessage", "Internal Server Error.");
			return new ResponseEntity<>(ret, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(value = "/registerUser", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> updateValidatedUser(HttpServletRequest request, @RequestBody RegisterInputDTO body) throws Exception {
		logger.info("> User update process for newly registered users start!");
		Map<String, Object> result = new HashMap<String, Object>();
		String NRUuid = body.getNRUuid();
		String userUuid = "";

		// 회원, 비회원 판별
		String header = request.getHeader("Authorization");
		if (header == null) {																// 비회원일 때 토큰 null
			logger.info("header.Authorization is null. No token given.");
			result.put("resultMessage", "header.Authorization is null. No token given.");
			return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
		} else {																			// 회원이면 토큰 파싱하여 유저 아이디 꺼냄
			String token = request.getHeader("Authorization").replace("Bearer ","");
			try {
				Map<String, Object> parseInfo = jwtTokenUtil.getUserParseInfo(token);
				userUuid = parseInfo.get("userUuid").toString();
				logger.info("User UUID from header token : " + userUuid);
			} catch (Exception e) {
				logger.info("error : cannot parse jwt token, " + e.getMessage());
				result.put("error", e.getMessage());
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		String resultMessage = userInfoService.updateValidatedUserInfo(userUuid, NRUuid);

		result.put("resultMessage", resultMessage);
		if (resultMessage.contains("error")) {
			if (resultMessage.contains("NOT_FOUND"))
				return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
			else
				return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else
			return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
