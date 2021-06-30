package com.tmax.eTest.Report.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tmax.eTest.Contents.model.Problem;
import com.tmax.eTest.Contents.model.ProblemUKRelation;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.Report.dto.MiniTestResultDTO;
import com.tmax.eTest.Report.dto.lrs.GetStatementInfoDTO;
import com.tmax.eTest.Report.dto.lrs.StatementDTO;
import com.tmax.eTest.Report.dto.triton.TritonDataDTO;
import com.tmax.eTest.Report.dto.triton.TritonRequestDTO;
import com.tmax.eTest.Report.dto.triton.TritonResponseDTO;
import com.tmax.eTest.Report.util.LRSAPIManager;
import com.tmax.eTest.Report.util.TritonAPIManager;
import com.tmax.eTest.Test.model.UserEmbedding;
import com.tmax.eTest.Test.model.UserKnowledge;
import com.tmax.eTest.Test.repository.UserEmbeddingRepository;
import com.tmax.eTest.Test.repository.UserKnowledgeRepository;

@Service
public class MiniTestReportService {
	
	@Autowired
	LRSAPIManager lrsAPIManager;
	
	@Autowired
	TritonAPIManager tritonAPIManager;
	
	@Autowired
	ProblemRepository problemRepo;
	
	@Autowired
	UserKnowledgeRepository userKnowledgeRepo;
	
	@Autowired
	UserEmbeddingRepository userEmbeddingRepo;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	

	public MiniTestResultDTO getMiniTestResult(String userId)
	{
		MiniTestResultDTO result = new MiniTestResultDTO();
		
		// Mini Test 관련 문제 풀이 정보 획득.
		List<StatementDTO> miniTestRes = getMiniTestResultInLRS(userId);
		int diagQuestionInfo[] = calculateDiagQuestionInfo(miniTestRes);
		List<Problem> probInfos = getProblemInfos(miniTestRes);
		TritonResponseDTO tritonResponse = getUnderstandingScoreInTriton(miniTestRes, probInfos);
		TritonDataDTO embeddingData = null;
		TritonDataDTO masteryData = null;
		
		for(TritonDataDTO dto : tritonResponse.getOutputs())
		{
			if(dto.getName().equals(""))
			{
				
			}
			else if(dto.getName().equals(""))
			{
				
			}
		}
		
		
		result.initForDummy();
		result.setDiagnosisQuestionInfo(diagQuestionInfo);
		
		saveUKAndEmbedding(userId, tritonResponse);
		
		return result;
	}
	
	private Map<Integer, Float> makeUnderstandingScore(TritonResponseDTO triton)
	{
		Map<Integer, Float> result = new HashMap<Integer, Float>();
		
		
		
		return result;
	}
	
	private List<StatementDTO> getMiniTestResultInLRS(String userID)
	{
		List<StatementDTO> result = new ArrayList<>();
		GetStatementInfoDTO statementInput = new GetStatementInfoDTO();
		statementInput.pushUserId(userID);
		statementInput.pushSourceType("mini_test_question");
		statementInput.pushSourceType("diagnosis");
		statementInput.pushSourceType("question");
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
	
	private List<Problem> getProblemInfos(List<StatementDTO> miniTestResult)
	{
		List<Integer> probIdList = new ArrayList<>();
		List<Problem> probList = new ArrayList<>();
		
		for(StatementDTO dto : miniTestResult)
		{
			try
			{
				int probId = Integer.parseInt(dto.getSourceId());
				probIdList.add(probId);
				probList = problemRepo.findAllById(probIdList);
			}
			catch(Exception e)
			{
				logger.info("getUnderstandingScoreInTriton : "+e.toString()+" id : "+dto.getSourceId()+" error!");
			}
		}
		
		
		// problem 관련 정보를 가공하여 TritonInput 화.
		return probList;
	}
	
	private TritonResponseDTO getUnderstandingScoreInTriton(
			List<StatementDTO> miniTestResult,
			List<Problem> probInfos)
	{
		// first process : 문제별 PK 얻어오기.
		Map<Integer, Integer> isCorrectMap = new HashMap<>();
		for(StatementDTO dto : miniTestResult)
		{
			try
			{
				int probId = Integer.parseInt(dto.getSourceId());
				isCorrectMap.put(probId, dto.getIsCorrect());
			}
			catch(Exception e)
			{
				logger.info("getUnderstandingScoreInTriton : "+e.toString()+" id : "+dto.getSourceId()+" error!");
			}
		}
		
		// first process : 문제별 PK 얻어오기.
		
		TritonRequestDTO tritonReq = new TritonRequestDTO();
		
		tritonReq.initDefault();
		
		List<Object> ukList = new ArrayList<>();
		List<Object> isCorrectList = new ArrayList<>();
		List<Object> diffcultyList = new ArrayList<>();
		
		for(Problem prob : probInfos)
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
		tritonReq.pushInputData("DifficultyList", "INT32", diffcultyList);
		
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
	
	private void saveUKAndEmbedding(String userId, TritonResponseDTO triton)
	{
		String userEmbedding = null;
		for(TritonDataDTO dto : triton.getOutputs())
		{
			if(dto.getName().compareTo("Embeddings") == 0)
			{
				userEmbedding = (String) dto.getData().get(0);
				logger.info(userId);
				logger.info(userEmbedding);
				UserEmbedding updateEmbedding = new UserEmbedding();
				updateEmbedding.setUserUuid(userId);
				updateEmbedding.setUserEmbedding(userEmbedding);
				updateEmbedding.setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
				logger.info(updateEmbedding.toString());
				userEmbeddingRepo.save(updateEmbedding);
			}
			if(dto.getName().compareTo("Mastery") == 0)
			{
				try {
					JsonObject masteryJson = (JsonObject) JsonParser.parseString((String) dto.getData().get(0));
					Set<UserKnowledge> userKnowledgeSet = new HashSet<UserKnowledge>();
			
					masteryJson.keySet().forEach(ukId -> {
						int ukUuid = Integer.parseInt(ukId);
						UserKnowledge userKnowledge = new UserKnowledge();
						userKnowledge.setUserUuid(userId);
						userKnowledge.setUkUuid(ukUuid);
						userKnowledge.setUkId(ukId);
						userKnowledge.setUkMastery(masteryJson.get(ukId).getAsFloat());
						userKnowledge.setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
						userKnowledgeSet.add(userKnowledge);
				
					});
					
					userKnowledgeRepo.saveAll(userKnowledgeSet);
				}
				catch(Exception e)
				{
					logger.info(e.toString());
				}
			}
		}		
	}
	
}
