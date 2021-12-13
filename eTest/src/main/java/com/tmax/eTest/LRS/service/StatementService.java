package com.tmax.eTest.LRS.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.model.Statement;
import com.tmax.eTest.LRS.model.StatementSpecs;
import com.tmax.eTest.LRS.repository.StatementRepository;
import com.tmax.eTest.LRS.util.JWTUtil;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class StatementService {

	@Autowired
	private StatementRepository statementRepo;

	public List<Integer> saveStatementList(List<StatementDTO> inputList) {
		List<Integer> results = new ArrayList<>();
		int result = 0;
		for (StatementDTO input : inputList) {
			Statement dao = new Statement(input);
			if (dao != null) {
				try {
					statementRepo.save(dao);
				}
				catch(IllegalArgumentException e){ 
					// error!
					log.error("error statement info : " 
							+input.getUserId()+" "
							+input.getActionType()+" "
							+input.getUserAnswer()+" "
							+input.getStatementDate().toString()+" "
							+input.getSourceId()+" "
							+input.getSourceType());
					
					results.add(result);
				}
				
				result++;
			}
		}
		
		return results;
	}

	public List<StatementDTO> getStatementList(
			GetStatementInfoDTO searchInfo, 
			boolean isAscTimestamp, 
			boolean checkIsNotDeleted) {
		
		List<Statement> dbRes = statementRepo.findAll(
				StatementSpecs.searchStatement(searchInfo, isAscTimestamp, checkIsNotDeleted));
		List<StatementDTO> result = new ArrayList<StatementDTO>();
		
		log.info(result.toString());

		int recentNum = (searchInfo.getRecentStatementNum() != null)
				?searchInfo.getRecentStatementNum()
				:dbRes.size();
		int idx = (dbRes.size() - recentNum);
		for (int i = (idx>=0)?idx:0; i < dbRes.size(); i++) {
			result.add(new StatementDTO(dbRes.get(i)));
		}

		return result;
	}
	
	public boolean updateUserID(
			String recentUserID,
			String changeUserID)
	{
		boolean result = true;
		
		GetStatementInfoDTO searchInfo = new GetStatementInfoDTO();
		searchInfo.pushUserId(recentUserID);
		
		List<Statement> dbRes = statementRepo.findAll(
				StatementSpecs.searchStatement(searchInfo, false, false));
		
		for(Statement state : dbRes)
			state.setUserId(changeUserID);
		
		statementRepo.saveAll(dbRes);
		
		return result;
	}
	
	public boolean setStatementDisable(String userIdToken)
	{
		String userID = null;
		
		userID = JWTUtil.getJWTPayloadField(userIdToken, "userID");

		if(userID != null)
		{
			GetStatementInfoDTO searchInfo = new GetStatementInfoDTO();
			searchInfo.pushUserId(userID);
			
			List<Statement> statements = statementRepo.findAll(StatementSpecs.searchStatement(searchInfo, false, false));
			
			for(Statement dao : statements)
				dao.setIsDeleted(1);
			
			statementRepo.saveAll(statements);

			return true;
		}
		else
			return false;
	}
	
	// sourceId == nullable
	public boolean setStatementDisable(String userId, String sourceType, String sourceId)
	{
		if(userId != null && sourceType != null)
		{
			GetStatementInfoDTO searchInfo = new GetStatementInfoDTO();
			searchInfo.pushUserId(userId);
			searchInfo.pushSourceType(sourceType);
			
			if(sourceId != null)
				searchInfo.pushSourceId(sourceId);
			
			List<Statement> statements = statementRepo.findAll(StatementSpecs.searchStatement(searchInfo, false, false));
			
			for(Statement dao : statements)
				dao.setIsDeleted(1);
			
			statementRepo.saveAll(statements);

			return true;
		}
		else
			return false;
	}
	
}
