package com.tmax.eTest.Test.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Test.common.DemoChapterPartMapper;
import com.tmax.eTest.Test.model.UserMaster;

@Service
public class ProblemService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	@Autowired
	UserInfoService userService;
	
	
	public List<Map<String, String>> getNextProblemSet(String userId, String diagType, String part, String is_adaptive){

		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		
		
		return list;
	}
	
	
}
