package com.tmax.eTest.Test.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Test.service.ProblemService;
import com.tmax.eTest.Test.service.UserInfoService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserInfoController {
	
	@Autowired
	UserInfoService userinfosvc;
	
	@PutMapping(value = "/userInfo/{id}")
	public ResponseEntity<Object> putUserInfo(@PathVariable(value="id") String userId) throws Exception {
		Map<String, Object> ret = new HashMap<String, Object>();
		try{
			String res = userinfosvc.updateUserInfo(userId);
			ret.put("resultMessage", res);
			return new ResponseEntity<>(ret, HttpStatus.OK);

		} catch (Exception E){
			ret.put("resultMessage", "Internal Server Error.");
			return new ResponseEntity<>(ret, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
