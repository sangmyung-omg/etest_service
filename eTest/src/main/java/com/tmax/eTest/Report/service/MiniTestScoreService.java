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
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.Report.dto.MiniTestResultDTO;
import com.tmax.eTest.Report.dto.lrs.GetStatementInfoDTO;
import com.tmax.eTest.Report.dto.lrs.StatementDTO;
import com.tmax.eTest.Report.dto.triton.TritonDataDTO;
import com.tmax.eTest.Report.dto.triton.TritonRequestDTO;
import com.tmax.eTest.Report.dto.triton.TritonResponseDTO;
import com.tmax.eTest.Report.util.LRSAPIManager;
import com.tmax.eTest.Report.util.SNDCalculator;
import com.tmax.eTest.Report.util.StateAndProbProcess;
import com.tmax.eTest.Report.util.TritonAPIManager;
import com.tmax.eTest.Report.util.UKScoreCalculator;
import com.tmax.eTest.Common.model.report.MinitestReport;
import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.Common.model.user.UserEmbedding;
import com.tmax.eTest.Common.model.user.UserKnowledge;
import com.tmax.eTest.Test.repository.MinitestReportRepo;
import com.tmax.eTest.Test.repository.UserEmbeddingRepository;
import com.tmax.eTest.Test.repository.UserKnowledgeRepository;

@Service
public class MiniTestScoreService {

	@Autowired
	LRSAPIManager lrsAPIManager;
	@Autowired
	TritonAPIManager tritonAPIManager;

	@Autowired
	StateAndProbProcess stateAndProbProcess;
	@Autowired
	UKScoreCalculator scoreCalculator;

	@Autowired
	ProblemRepository problemRepo;
	@Autowired
	UserKnowledgeRepository userKnowledgeRepo;
	@Autowired
	UserEmbeddingRepository userEmbeddingRepo;
	@Autowired
	MinitestReportRepo minitestReportRepo;
	
	@Autowired SNDCalculator sndCalculator;

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	public MiniTestResultDTO getMiniTestResult(String userId, String probSetId) {
		MiniTestResultDTO result = new MiniTestResultDTO();
		result.initForDummy();

		// Mini Test 관련 문제 풀이 정보 획득.
		List<StatementDTO> miniTestRes = getMiniTestResultInLRS(userId, probSetId);
		List<Problem> probInfos = getProblemInfos(miniTestRes);
		Map<String, List<Object>> probInfoForTriton = stateAndProbProcess.makeInfoForTriton(miniTestRes, probInfos);
		TritonResponseDTO tritonResponse = tritonAPIManager.getUnderstandingScoreInTriton(probInfoForTriton);

		TritonDataDTO embeddingData = null;
		TritonDataDTO masteryData = null;
		
		List<List<String>> diagQuestionInfo = stateAndProbProcess.calculateDiagQuestionInfo(miniTestRes);
		result.setDiagnosisQuestionInfo(diagQuestionInfo);

		if(tritonResponse != null)
		{
			for (TritonDataDTO dto : tritonResponse.getOutputs()) {
				if (dto.getName().equals("Embeddings")) {
					embeddingData = dto;
				} else if (dto.getName().equals("Mastery")) {
					masteryData = dto;
				}
			}
	
			if (embeddingData != null && masteryData != null) {
				Map<Integer, UkMaster> usedUkMap =stateAndProbProcess.makeUsedUkMap(probInfos);
				Map<Integer, Float> ukScoreMap = scoreCalculator.makeUKScoreMap(masteryData);
				float ukModiRatio = 1.5f, ukModiDif = 0.1f;
				
				ukScoreMap.forEach((ukUuid, score) -> {
					float modScore = score*ukModiRatio - ukModiDif;
					modScore = (modScore > 1)? 1.f : (modScore <= 0.05) ? 0.05f : modScore;
					ukScoreMap.put(ukUuid, modScore);
				});
				
				List<List<String>> partScoreList = scoreCalculator.makePartScore(usedUkMap, ukScoreMap);
				Map<String, List<List<String>>> partUkDetail = scoreCalculator.makePartUkDetail(usedUkMap, ukScoreMap, partScoreList);
				int setNum = 0;
				
				if(probInfos.size() > 0)
					setNum = 1;//probInfos.get(0).getTestInfo().getSetNum(); jinhyung edit
				
				result.setPartUnderstanding(partScoreList);
				result.setPartUkDetail(partUkDetail);
				//result.setWeakPartDetail(weakPartDetail);
	
				float avg = 0;
				for (List<String> part : partScoreList) {
					avg += Float.parseFloat(part.get(2));
				}
				
				int ukAvgScore = 0;
				
				if(partScoreList.size() > 0)
					ukAvgScore = Math.round(avg / partScoreList.size());
	
				result.setScore(ukAvgScore);
				result.setPercentage(sndCalculator.calculateForMiniTest(ukAvgScore));
	
				saveUserUKInfo(userId, ukScoreMap);
				saveMinitestReport(userId, ukAvgScore, diagQuestionInfo, setNum);
				
			}
		}
		

		return result;
	}
	
	private void saveMinitestReport(
			String id, 
			float ukAvgScore, 
			List<List<String>> diagQuestionInfo,
			int setNum)
	{
		MinitestReport miniReport = new MinitestReport();
		
		miniReport.setMinitestId(UUID.randomUUID().toString());
		miniReport.setUserUuid(id);
		miniReport.setAvgUkMastery((float) ukAvgScore);
		miniReport.setCorrectNum(diagQuestionInfo.get(0).size());
		miniReport.setWrongNum(diagQuestionInfo.get(1).size());
		miniReport.setDunnoNum(diagQuestionInfo.get(2).size());
		miniReport.setSetNum(setNum);
		miniReport.setMinitestDate(Timestamp.valueOf(LocalDateTime.now()));
		
		minitestReportRepo.save(miniReport);
	}


	private List<StatementDTO> getMiniTestResultInLRS(String userID, String probSetId) {
		List<StatementDTO> result = new ArrayList<>();
		GetStatementInfoDTO statementInput = new GetStatementInfoDTO();
		statementInput.pushUserId(userID);
		statementInput.pushSourceType("mini_test_question");
		statementInput.pushActionType("submit");
		
		if(probSetId != null)
			statementInput.pushExtensionStr(probSetId);

		try {
			result = lrsAPIManager.getStatementList(statementInput);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private List<Problem> getProblemInfos(List<StatementDTO> miniTestResult) {
		List<Integer> probIdList = new ArrayList<>();
		List<Problem> probList = new ArrayList<>();

		for (StatementDTO dto : miniTestResult) {
			try {
				int probId = Integer.parseInt(dto.getSourceId());
				probIdList.add(probId);
				probList = problemRepo.findAllById(probIdList);
			} catch (Exception e) {
				logger.info(
					"getProblemInfos : " + e.toString() + " id : " + dto.getSourceId() + " error!");
			}
		}

		// problem 관련 정보를 가공하여 TritonInput 화.
		return probList;
	}

	
	
	private void saveUserUKInfo(String userId, Map<Integer, Float> ukScoreList) {

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
			userKnowledgeRepo.saveAll(userKnowledgeSet);
		} catch (Exception e) {
			logger.info(e.toString());
		}
	}
	
	private void saveUserEmbeddingInfo(String userId, TritonDataDTO embedding)
	{
		String userEmbedding = (String) embedding.getData().get(0);
		UserEmbedding updateEmbedding = new UserEmbedding();
		updateEmbedding.setUserUuid(userId);
		updateEmbedding.setUserEmbedding(userEmbedding);
		updateEmbedding.setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
		logger.info(updateEmbedding.toString());
		
		try {
			userEmbeddingRepo.save(updateEmbedding);
		} catch (Exception e) {
			logger.info(e.toString());
		}
	}

	

}
