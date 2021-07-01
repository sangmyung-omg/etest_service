package com.tmax.eTest.Test.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Test.dto.UserDTO;
import com.tmax.eTest.Test.service.UserInfoService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserInfoController {
	
	@Autowired
	UserInfoService userService;
	
	@PutMapping(value = "/user", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> updateUserInfo(@RequestBody UserDTO input) throws Exception {
		String user_uuid = input.getUser_uuid();
		
		Map<String, String> map = new HashMap<String, String>();
		
		if (user_uuid == null || user_uuid.equalsIgnoreCase("")) {
			map.put("error", "No user_uuid.");
			return new ResponseEntity<Object>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		String queryResult = userService.updateUserInfo(user_uuid);
		
		map.put("resultMessage", queryResult);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
