package com.tmax.eTest.Test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.report.MinitestReport;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Test.repository.DiagnosisReportRepo;
import com.tmax.eTest.Test.repository.MinitestReportRepo;
import com.tmax.eTest.Test.repository.UserRepository;

@Service
public class UserInfoService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DiagnosisReportRepo diagReportRepo;
	
	@Autowired
	private MinitestReportRepo miniReportRepo;

	public String updateUserInfo(String user_uuid) {
		UserMaster user = new UserMaster();
		if (user_uuid == null || user_uuid.equalsIgnoreCase("")) {
			logger.info("No user_uuid.");
			return "No user ID";
		}
		
		user.setUserUuid(user_uuid);
		user.setUserType("etest_BetaTest");
		
		userRepository.save(user);
		
		logger.info("Successfully updated user ID");
		return "Successfully updated";
	}
	
	public String updateDiagnosisReportValues(DiagnosisReport dto) {
		if (dto.getUserUuid() == null || dto.getUserUuid().equalsIgnoreCase("")) {
			logger.info("No user ID. Updating report info is not occurred.");
			return "No user ID";
		}
		diagReportRepo.save(dto);
		return "Successfully updated";
	}
	
	public String updateMinitestReportValues(MinitestReport dto) {
		if (dto.getUserUuid() == null || dto.getUserUuid().equalsIgnoreCase("")) {
			logger.info("No user ID. Updating report info is not occurred.");
			return "No user ID";
		}
		miniReportRepo.save(dto);
		return "Successfully updated";
	}
}
