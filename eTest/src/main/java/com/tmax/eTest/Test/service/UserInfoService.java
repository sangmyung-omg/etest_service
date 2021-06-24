package com.tmax.eTest.Test.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Test.model.UserMaster;
import com.tmax.eTest.Test.repository.UserRepository;

@Service
public class UserInfoService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());


	@Autowired
	private UserRepository userRepository;

	public List<String> getChapterNameList(String grade, String semester) {
		List<String> list = new ArrayList<String>();

		return list;
	}

	public String updateUserCurrentInfo(String userId, String grade, String semester, String chapter) {

		return "Successfully updated";
	}

	public UserMaster getUserInfo(String userId) {
		UserMaster result = new UserMaster();
		List<String> input = new ArrayList<String>();
		input.add(userId);
		logger.info("Getting user basic info...");
		List<UserMaster> queryList = (List<UserMaster>) userRepository.findAllById(input);
		System.out.println("####################" + input + ", " + queryList);
		if (queryList.size() != 0 && queryList != null) {
			result = queryList.get(0);			
		}
		return result;
	}

	public Map<String, String> updateExamInfo(Map<String, Object> input) {
		Map<String, String> output = new HashMap<String, String>();
		String userId = (String) input.get("userId");
		String examType = (String) input.get("examType");
		String examDate = (String) input.get("examDate");
		String targetScore = (String) input.get("targetScore");

		if (!examType.equals("mid") && !examType.equals("final")) {
			output.put("resultMessage", "'examType' should be either 'mid' or 'final'.");
			return output;
		}

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime examDateTime;
		try {
			examDateTime = LocalDate.parse(examDate, dtf).atStartOfDay();
		} catch (DateTimeParseException e) {
			output.put("resultMessage", "'examDate' should be in shape of 'yyyy-MM-dd'.");
			return output;
		}
		
		Integer targetScoreInt;
		try {
			targetScoreInt = Integer.parseInt(targetScore);
		} catch (NumberFormatException e) {
			output.put("resultMessage", "'targetScore' should be a string of integer.");
			return output;
		}
				
		UserMaster userDAO = userRepository.findById(userId).orElse(new UserMaster());
		userDAO.setUserUuid(userId);
		userDAO.setExamType(examType);
		userDAO.setExamStartDate(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
		userDAO.setExamDueDate(Timestamp.valueOf(examDateTime));
		userDAO.setExamTargetScore(targetScoreInt);

		userRepository.save(userDAO);
		output.put("resultMessage", "Successfully update user exam info.");

		return output;
	}
}
