package com.tmax.eTest.LRS.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.SourceType;
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


@Service
public class StatementService {
	 private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

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
				catch(Exception e){ 
					// error!
					logger.error("saveStatementList error : "+e.toString());
					logger.error("error statement info : " 
							+input.getUserId()+" "
							+input.getActionType()+" "
							+input.getUserAnswer()+" "
							+input.getTimestamp()+" "
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
		
		logger.info(result.toString());

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
		
		try {
			userID = JWTUtil.getJWTPayloadField(userIdToken, "userID");
			logger.info("setStatementDisable userID : "+userID);
		}
		catch(Exception e)
		{
			logger.info("setStatementDisable error : " + e.toString());
		}
		
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
	
}
