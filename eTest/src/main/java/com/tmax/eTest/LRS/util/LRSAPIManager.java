package com.tmax.eTest.LRS.util;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.service.StatementService;

/**
 * Call StatementList GET API from LRS Server
 * 
 * @author sangheonLee
 */
@Component
@PropertySource("classpath:application.properties")
public class LRSAPIManager {

	private final Logger logger = LoggerFactory.getLogger("LRSAPIManager");
	
	@Autowired
	StatementService statementService;

	public List<Integer> saveStatementList(List<StatementDTO> input) throws ParseException {
		return statementService.saveStatementList(input);
	}

	public List<StatementDTO> getStatementList(GetStatementInfoDTO input) throws ParseException {
		return statementService.getStatementList(input, true, true);
	}
	
	public boolean updateStatementUserID(String recentUserID, String changeUserID) {
		return statementService.updateUserID(recentUserID, changeUserID);
	}
	
	// if videoId == null => all bound
	public boolean disableVideoStatement(String userId, String videoId)
	{
		return statementService.setStatementDisable(userId, "video", videoId);
	}
	
	// if article == null => all bound
	public boolean disableArticleStatement(String userId, String articleId)
	{
		return statementService.setStatementDisable(userId, "article", articleId);
	}
	
	

	public LRSAPIManager() {
	}

}
