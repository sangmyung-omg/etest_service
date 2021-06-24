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
import com.tmax.eTest.Test.dto.ChapterMasteryDTO;
import com.tmax.eTest.Test.dto.UKDTO;
import com.tmax.eTest.Test.dto.UKMasteryDTO;
import com.tmax.eTest.Test.model.UkMaster;
import com.tmax.eTest.Test.model.UserKnowledge;
import com.tmax.eTest.Test.repository.UkRepository;
import com.tmax.eTest.Test.repository.UserKnowledgeRepository;

@Service
public class CurriculumService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	UkRepository ukRepository;
	
	@Autowired
	UserKnowledgeRepository userKnowledgeRepository;
	
	@Autowired
	MasteryService masteryService;
		
	public Map<String, Object> getChapterMastery(String userId, List<String> ukIdList){

		Map<String, Object> re = new HashMap<String, Object>();

		re.put("resultMessage", "successfully returned");
		return re;
	}
	
	public List<String> getChapterNameList(String grade, String semester) {
		List<String> list = new ArrayList<String>();

		return list;
	}
}
