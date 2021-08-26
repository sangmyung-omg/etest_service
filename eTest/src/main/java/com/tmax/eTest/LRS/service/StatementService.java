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
	
//	public InfoForMasteryDTO getInfoForMastery(String token)
//	{
//		
//		String userID = null;
//		
//		try {
//			userID = JWTUtil.getJWTPayloadField(token, "userID");
//			logger.info("getInfoForMastery userID : "+userID);
//		}
//		catch(Exception e)
//		{
//			logger.info("getInfoForMastery error : " + e.toString());
//		}
//		
//		if(userID != null)
//		{
//			InfoForMasteryDTO result = new InfoForMasteryDTO();
//			GetStatementInfoDTO searchInfo = new GetStatementInfoDTO();
//			
//			searchInfo.pushUserIdList(userID);
//			searchInfo.pushActionType("submit");
//			
//			List<StatementDAO> dbRes = statementRepo.findAll(StatementSpecs.searchStatement(searchInfo, false, false));
//			int pushNum = 0;
//			
//			for(int i = 0; i < dbRes.size(); i++)
//			{
//				StatementDAO row = dbRes.get(i);
//				String correctStr = "true";
//				
//				// Correct 정보가 있고 disable 되지 않은 statement에 대해 처리.
//				if(row.getIsCorrect() != null && row.getIsDeleted() == 0)
//				{
//					if(row.getIsCorrect() == 0)
//					{
//						if (row.getUserAnswer() == "PASS" || row.getUserAnswer() == "pass")
//							correctStr = "pass";
//						else
//							correctStr = "false";
//					}
//					
//					if(result.pushData(row.getSourceId(), correctStr))
//						pushNum++;
//					
//					if(pushNum >= 200)
//						break;
//				}
//			}
//			
//			return result;
//		}
//		else
//			return null;
//	}

//	public List<LagTimeInfoDTO> getLagTimeInfo(String userId, Integer recentQuestionNum) {
//		List<LagTimeInfoDTO> serviceRes = new ArrayList<>();
//		String[] actionArr = {"submit", "enter"};
//		String[] sourceArr = {"type_question", "supple_question", "mid_exam_question", "trial_exam_question",
//				"retry_question", "wrong_answer_question", "starred_question", "diagnosis"};
//		
//		GetStatementInfoDTO dto = new GetStatementInfoDTO();
//		List<String> userIdList = new ArrayList<>();
//		userIdList.add(userId);
//		
//		dto.setUserIdList(userIdList);
//		dto.setActionTypeList(Arrays.asList(actionArr));
//		dto.setSourceTypeList(Arrays.asList(sourceArr));
//
//		List<StatementDTO> dbRes = getStatementList(dto, false, false);
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
//		
//		for(int i = 0; i < dbRes.size()-2; i++)
//		{
//			StatementDTO[] nowState = {dbRes.get(i), dbRes.get(i+1), dbRes.get(i+2)};
//			if(nowState[0].getActionType() == "submit"
//				&& nowState[1].getActionType() == "enter"
//				&& nowState[2].getActionType() == "submit"
//				&& nowState[1].getSourceId() == nowState[2].getSourceId())
//			{
//				LagTimeInfoDTO lagInfo = new LagTimeInfoDTO();
//				
//				lagInfo.setIsCorrect(nowState[2].getIsCorrect()==1);
//				lagInfo.setQuestionId(nowState[2].getSourceId());
//				
//				try {
//					long prevEndTime = df.parse(nowState[0].getTimestamp()).getTime();
//					long nowStartTime = df.parse(nowState[1].getTimestamp()).getTime();
//					long nowEndTime = df.parse(nowState[2].getTimestamp()).getTime();
//					
//					lagInfo.setLagTime(nowStartTime-prevEndTime);
//					lagInfo.setElaspedTime(nowEndTime-nowStartTime);
//					
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				serviceRes.add(lagInfo);
//				i += 2;
//			}
//		}
//
//		return serviceRes;
//	}
}
