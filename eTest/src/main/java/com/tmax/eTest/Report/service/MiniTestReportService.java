package com.tmax.eTest.Report.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Contents.model.ProblemUKRelation;
import com.tmax.eTest.Report.dto.MiniTestResultDTO;
import com.tmax.eTest.Report.dto.lrs.GetStatementInfoDTO;
import com.tmax.eTest.Report.dto.lrs.StatementDTO;
import com.tmax.eTest.Report.repository.ProbUKRelRepo;
import com.tmax.eTest.Report.repository.ProbUKRelSpecs;
import com.tmax.eTest.Report.util.LRSAPIManager;
import com.tmax.eTest.Report.util.TritonAPIManager;

@Service
public class MiniTestReportService {
	
	@Autowired
	LRSAPIManager lrsAPIManager;
	
	@Autowired
	TritonAPIManager tritonAPIManager;
	
	@Autowired
	private ProbUKRelRepo probUKRelRepo;
	
	@Autowired
	private ProbUKRelSpecs probUKRelSpecs;
	
	private List<StatementDTO> getMiniTestResultInLRS(String userID)
	{
		List<StatementDTO> result = new ArrayList<>();
		GetStatementInfoDTO statementInput = new GetStatementInfoDTO();
		statementInput.pushUserId(userID);
		statementInput.pushSourceType("mini_test_question");
		statementInput.pushSourceType("diagnosis");
		statementInput.pushActionType("submit");
		
		try {
			result = lrsAPIManager.getStatementList(statementInput);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	private int[] calculateDiagQuestionInfo(List<StatementDTO> miniTestResult)
	{
		int diagQuestionInfo[] = {0,0,0};
		
		for(StatementDTO state : miniTestResult)
		{
			if(state.getIsCorrect() == 1)
				diagQuestionInfo[0]++;
			else if(state.getUserAnswer() != "PASS")
				diagQuestionInfo[1]++;
			else
				diagQuestionInfo[2]++;
		}
		
		return diagQuestionInfo;
	}
	
	private void getUnderstandingScoreInTriton(List<StatementDTO> miniTestResult)
	{
		// first process : 문제별 PK 얻어오기.
		List<ProblemUKRelation> ukResult =  probUKRelRepo.findAll(probUKRelSpecs.searchProbUKRelFromStatementList(miniTestResult));
		
		
	}
	
	public MiniTestResultDTO getMiniTestResult(String userId)
	{
		MiniTestResultDTO result = new MiniTestResultDTO();
		
		// Mini Test 관련 문제 풀이 정보 획득.
		List<StatementDTO> miniTestRes = getMiniTestResultInLRS(userId);
		int diagQuestionInfo[] = calculateDiagQuestionInfo(miniTestRes);
	
		
		result.setDiagnosisQuestionInfo(diagQuestionInfo);
		
		
		return result;
	}
}
