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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Test.model.UserMaster;
import com.tmax.eTest.Test.repository.UserRepository;

@Service
public class UserInfoService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private UserRepository userRepository;

	public String updateUserInfo(String user_uuid) {
		UserMaster user = new UserMaster();
		if (user_uuid == null || user_uuid.equalsIgnoreCase("")) {
			logger.info("No user_uuid.");
			return "No user ID";
		}
		
		user.setUserUuid(user_uuid);
		user.setUserType("etest_KOFIA");
		
		userRepository.save(user);
		
		logger.info("Successfully updated user ID");
		return "Successfully updated";
	}
}
