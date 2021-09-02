package com.tmax.eTest.Test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.report.MinitestReport;
import com.tmax.eTest.Common.model.user.UserKnowledge;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.LRS.service.StatementService;
import com.tmax.eTest.Test.repository.DiagnosisReportRepo;
import com.tmax.eTest.Test.repository.MinitestReportRepo;
import com.tmax.eTest.Test.repository.UserKnowledgeRepository;
import com.tmax.eTest.Test.repository.UserRepository;

@Slf4j
@Service
public class UserInfoService {

	@Autowired
	private StatementService statementService;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DiagnosisReportRepo diagReportRepo;
	
	@Autowired
	private MinitestReportRepo miniReportRepo;

	@Autowired
	private UserKnowledgeRepository userKnowledgeRepository;

	public String updateUserInfo(String user_uuid) {
		UserMaster user = new UserMaster();
		if (user_uuid == null || user_uuid.equalsIgnoreCase("")) {
			log.info("No user_uuid.");
			return "No user ID";
		}
		
		user.setUserUuid(user_uuid);
		user.setUserType("etest_BetaTest");
		
		userRepository.save(user);
		
		log.info("Successfully updated user ID");
		return "Successfully updated";
	}
	
	public String updateDiagnosisReportValues(DiagnosisReport dto) {
		if (dto.getUserUuid() == null || dto.getUserUuid().equalsIgnoreCase("")) {
			log.info("No user ID. Updating report info is not occurred.");
			return "No user ID";
		}
		diagReportRepo.save(dto);
		return "Successfully updated";
	}
	
	public String updateMinitestReportValues(MinitestReport dto) {
		if (dto.getUserUuid() == null || dto.getUserUuid().equalsIgnoreCase("")) {
			log.info("No user ID. Updating report info is not occurred.");
			return "No user ID";
		}
		miniReportRepo.save(dto);
		return "Successfully updated";
	}

	public String updateValidatedUserInfo(String userUuid, String NRUuid) throws Exception {
		// DIAGNOSIS_REPORT 테이블 update
		List<DiagnosisReport> recordList = diagReportRepo.findByUserUuid(NRUuid);
		if (recordList.size() == 0)
			return "error : NOT_FOUND. No records with the old UUID in DIAGNOSIS_REPORT.";
		for (DiagnosisReport record : recordList) {
			record.setUserUuid(userUuid);
		}
		diagReportRepo.saveAll(recordList);

		// STATEMENT 테이블 update
		if (!statementService.updateUserID(NRUuid, userUuid)) {
			return "error : LRS update failed";
		}

		return "Successfully updated the validated user info";
	}
}
