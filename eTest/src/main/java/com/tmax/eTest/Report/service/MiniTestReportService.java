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
import org.springframework.data.util.Pair;
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
			if(dto.getName().equals("Embeddings"))
			{
				embeddingData = dto;
			}
			else if(dto.getName().equals("Mastery"))
			{
				masteryData = dto;
			}
		}
		
		result.initForDummy();
		result.setDiagnosisQuestionInfo(diagQuestionInfo);
		
		if(embeddingData != null && masteryData != null)
		{
			Map<Integer, Float> ukScore = makeUKScore(masteryData);
			List<List<String>> partScoreList = makePartScore(ukScore);
			List<List<String>> weakPartDetail = makeWeakPartDetail(ukScore, partScoreList);
			
			result.setPartUnderstanding(partScoreList);
			result.setWeakPartDetail(weakPartDetail);
			
			float avg = 0;
			for(List<String> part : partScoreList)
			{
				avg += Float.parseFloat(part.get(2));
			}
			
			result.setScore(Math.round(avg/partScoreList.size()));
			
			saveUKAndEmbedding(userId, ukScore, embeddingData);
		}
		
		return result;
	}
	
	private String calculateUKScoreString(float ukScore)
	{
		if(ukScore >= 85.f)
			return "A";
		else if(ukScore >= 70.f)
			return "B";
		else if(ukScore >= 50.f)
			return "C";
		else if(ukScore >= 30.f)
			return "D";
		else if(ukScore >= 15.f)
			return "E";
		else
			return "F";
	}
	
	
	// partScore example ["partname", "C", "50"]
	private List<List<String>> makeWeakPartDetail(Map<Integer, Float> ukScore, List<List<String>> partScore)
	{
		List<List<String>> result = new ArrayList<>();
		Pair<String, Integer> lowestScorePart = Pair.of("NULL", 100);
		
		// check lowest score
		for(List<String> part : partScore)
		{
			String partName = part.get(0);
			int score = Integer.parseInt(part.get(2));
			
			if(lowestScorePart.getSecond() > score)
				lowestScorePart = Pair.of(partName, score);
		}
		
		// 파트 구불법 필요.
		String lowScorePartName = lowestScorePart.getFirst();
		int lowScore = lowestScorePart.getSecond();
		ukScore.forEach((uuid, score) ->{
			
			if( (lowScorePartName.equals("Part 1") && (uuid == 20 || uuid == 13 || uuid == 148))
				|| ( lowScorePartName.equals("Part 2") && (uuid == 217 || uuid == 254 || uuid == 234 || uuid == 235) ))
			{
				// example
				List<String> partUK = new ArrayList<>();
				
				partUK.add("UK "+uuid);
				partUK.add(calculateUKScoreString(lowScore));
				partUK.add("C");
				
				result.add(partUK);
			}

		});
		
		
		return result;
	}
	
	private List<List<String>> makePartScore(Map<Integer, Float> ukScore)
	{
		List<List<String>> result = new ArrayList<>();
		Map<String, Pair<Float, Integer>> partInfo = new HashMap<>();
		
		
		// 파트 구분법 필요.
		ukScore.forEach((ukUuid, score) -> {
			String partName = "Part 1";
			if(ukUuid > 150)
				partName = "Part 2";
			
			if(partInfo.get(partName) != null)
			{
				Pair<Float, Integer> scoreInfo = partInfo.get(partName);
				
				int partNum = scoreInfo.getSecond()+1;
				float avg = (scoreInfo.getFirst()*scoreInfo.getSecond()+score*100)/(float)partNum;
				
				partInfo.put(partName, Pair.of(avg, partNum));
				
			}
			else
			{
				Pair<Float, Integer> scoreInfo = Pair.of(score*100, 1);
				partInfo.put(partName, scoreInfo);
			}
	
		});
		
		partInfo.forEach((part, info) ->{
			List<String> partScore = new ArrayList<>();
			partScore.add(part);
			
			if(info.getFirst() >= 85.f)
				partScore.add("A");
			else if(info.getFirst() >= 70.f)
				partScore.add("B");
			else if(info.getFirst() >= 50.f)
				partScore.add("C");
			else if(info.getFirst() >= 30.f)
				partScore.add("D");
			else if(info.getFirst() >= 15.f)
				partScore.add("E");
			else
				partScore.add("F");
			
			partScore.add(String.valueOf(Math.round(info.getFirst())));
			
			result.add(partScore);
		});
		
		return result;
	}
	
	private Map<Integer, Float> makeUKScore(TritonDataDTO mastery)
	{
		Map<Integer, Float> result = new HashMap<Integer, Float>();
		
		JsonObject masteryJson = (JsonObject) JsonParser.parseString((String) mastery.getData().get(0));

		masteryJson.keySet().forEach(ukId -> {
			int ukUuid = Integer.parseInt(ukId);
			result.put(ukUuid, masteryJson.get(ukId).getAsFloat());	
		});
		
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
			else if(state.getUserAnswer().equalsIgnoreCase("pass"))
				diagQuestionInfo[2]++;
			else
				diagQuestionInfo[1]++;
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
	
	private void saveUKAndEmbedding(String userId, Map<Integer,Float> ukScoreList, TritonDataDTO embedding)
	{
		String userEmbedding = (String) embedding.getData().get(0);
		UserEmbedding updateEmbedding = new UserEmbedding();
		updateEmbedding.setUserUuid(userId);
		updateEmbedding.setUserEmbedding(userEmbedding);
		updateEmbedding.setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
		logger.info(updateEmbedding.toString());
		
		Set<UserKnowledge> userKnowledgeSet = new HashSet<UserKnowledge>();
		
		ukScoreList.forEach((ukUuid, score) -> {
			UserKnowledge userKnowledge = new UserKnowledge();
			userKnowledge.setUserUuid(userId);
			userKnowledge.setUkId(ukUuid);
			userKnowledge.setUkMastery(score);
			userKnowledge.setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
			userKnowledgeSet.add(userKnowledge);
	
		});
		
		try {
			userEmbeddingRepo.save(updateEmbedding);
			userKnowledgeRepo.saveAll(userKnowledgeSet);
		}
		catch(Exception e)
		{
			logger.info(e.toString());
		}	
	}
	
}
