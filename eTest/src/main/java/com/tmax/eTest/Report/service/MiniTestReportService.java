package com.tmax.eTest.Report.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Contents.model.Problem;
import com.tmax.eTest.Contents.model.ProblemUKRelation;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.Report.dto.MiniTestResultDTO;
import com.tmax.eTest.Report.dto.lrs.GetStatementInfoDTO;
import com.tmax.eTest.Report.dto.lrs.StatementDTO;
import com.tmax.eTest.Report.dto.triton.TritonRequestDTO;
import com.tmax.eTest.Report.dto.triton.TritonResponseDTO;
import com.tmax.eTest.Report.util.LRSAPIManager;
import com.tmax.eTest.Report.util.TritonAPIManager;

@Service
public class MiniTestReportService {
	
	@Autowired
	LRSAPIManager lrsAPIManager;
	
	@Autowired
	TritonAPIManager tritonAPIManager;
	
	@Autowired
	ProblemRepository problemRepo;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
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
	
	private TritonResponseDTO getUnderstandingScoreInTriton(List<StatementDTO> miniTestResult)
	{
		// first process : 문제별 PK 얻어오기.
		List<Long> probIdList = new ArrayList<>();
		Map<Long, Integer> isCorrectMap = new HashMap<>();
		for(StatementDTO dto : miniTestResult)
		{
			try
			{
				long probId = Long.parseLong(dto.getSourceId());
				probIdList.add(probId);
				isCorrectMap.put(probId, dto.getIsCorrect());
			}
			catch(Exception e)
			{
				logger.info("getUnderstandingScoreInTriton : "+e.toString()+" id : "+dto.getSourceId()+" error!");
			}
		}
		
		
		// problem 관련 정보를 가공하여 TritonInput 화.
		List<Problem> ukResult =  problemRepo.findAllById(probIdList);
		TritonRequestDTO tritonReq = new TritonRequestDTO();
		
		tritonReq.initDefault();
		
		List<Object> ukList = new ArrayList<>();
		List<Object> isCorrectList = new ArrayList<>();
		List<Object> diffcultyList = new ArrayList<>();
		
		for(Problem prob : ukResult)
		{
			List<ProblemUKRelation> probUKRels = prob.getProblemUKReleations();
			int diff = 1;
			int isCorrect = isCorrectMap.get(prob.getProbID());
			
			switch(prob.getDifficulty())
			{
			case "상":
				diff = 1;
				break;
			case "중":
				diff = 2;
				break;
			case "하":
				diff = 3;
				break;
			default:
				break;
			}
			
			for(ProblemUKRelation probUKRel : probUKRels)
			{
				int ukId = Integer.parseInt(probUKRel.getUkId().getUkId());
				ukList.add(ukId);
				isCorrectList.add(isCorrect);
				diffcultyList.add(diff);
			}
		}
		
		tritonReq.pushInputData("UKList", "INT32", ukList);
		tritonReq.pushInputData("IsCorrectList", "INT32", isCorrectList);
		tritonReq.pushInputData("DiffcultyList", "INT32", diffcultyList);
		
		// Triton에 데이터 요청.
		TritonResponseDTO tritonResponse = null;
		try {
			tritonResponse = tritonAPIManager.getInfer(tritonReq);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tritonResponse;
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
