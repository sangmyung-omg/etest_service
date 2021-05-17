package com.tmax.eTest.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmax.eTest.dao.TypeUkRepository;
import com.tmax.eTest.dao.UserDAO;
import com.tmax.eTest.dao.UserRepository;
import com.tmax.eTest.model.CardDTO;
import com.tmax.eTest.model.ProblemDTO;

@Service
public class CurriculumCardListService {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TypeUkRepository typeUkRepository;	
	
	public Map<String, Object> getCurriculumCardList(String userId, String date) throws Exception {
		Map<String, Object> output = new HashMap<String, Object>();

		// parse date and convert to Timestamp type
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Timestamp targetDate;
		try {
			LocalDateTime currentDateTime = LocalDate.parse(date, dtf).atStartOfDay();
			targetDate = Timestamp.valueOf(currentDateTime);
		} catch (DateTimeParseException e) {
			output.put("resultMessage", "'date' should be in shape of 'yyyy-MM-dd'.");
			return output;
		}

		// Load user information from USER_MASTER TB
		UserDAO userInfo;
		try {
			userInfo = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(userId));
		} catch (NoSuchElementException e) {
			output.put("resultMessage", String.format("userId = %s is not in USER_MASTER TB.", e.getMessage()));
			return output;
		}

		// Check whether user exam information is null
		String examType = userInfo.getExamType();
		Timestamp examStartDate = userInfo.getExamStartDate();
		Timestamp examDueDate = userInfo.getExamDueDate();
		String currentCurriculumId = userInfo.getCurrentCurriculumId();
		Integer examTargetScore = userInfo.getExamTargetScore();
		if (currentCurriculumId == null || examType == null || examStartDate == null || examDueDate == null
				|| examTargetScore == null) {
			output.put("resultMessage", "One of user's exam infomation is null. Call ExamInfo PUT service first.");
			return output;
		}

		// First day = 중간평가
		if (targetDate.equals(examStartDate)) {
			System.out.println("firstDate");
		}

		// Not first day = TypeUK 전체 리스트 로드 및 얼마나 완료했는지 확인
		// Load TypeUK of exam
		// final example 중단원 list --> 데모용
		List<String> sectionList = new ArrayList<String>(Arrays.asList("중등-중2-1학-03", "중등-중2-1학-04"));
		List<String> typeUkUuidList = typeUkRepository.findByCurriculum(sectionList);

		System.out.println();

		// dummy data
		CardDTO exampleCard = new CardDTO("유형UK", "유형UK1", new ArrayList<ProblemDTO>(
				Arrays.asList(new ProblemDTO("11", "12", "13"), new ProblemDTO("21", "22", "23"))));

		CardDTO exampleCard2 = new CardDTO("보충", "", new ArrayList<ProblemDTO>(
				Arrays.asList(new ProblemDTO("11", "12", "13"), new ProblemDTO("21", "22", "23"))));

		List<CardDTO> cardList = new ArrayList<CardDTO>();
//		cardList.add(objectMapper.writeValueAsString(exampleCard));
//		cardList.add(objectMapper.writeValueAsString(exampleCard2));
		cardList.add(exampleCard);
		cardList.add(exampleCard2);

		output.put("resultMessage", "Successfully return curriculum card list.");
		output.put("isComletable", "true");
		output.put("cardList", cardList);

		return output;
	}

}
