package com.tmax.eTest.Report.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Contents.model.Problem;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.Report.dto.MiniTestResultDTO;
import com.tmax.eTest.Report.dto.lrs.GetStatementInfoDTO;
import com.tmax.eTest.Report.dto.lrs.StatementDTO;
import com.tmax.eTest.Report.dto.triton.TritonDataDTO;
import com.tmax.eTest.Report.dto.triton.TritonRequestDTO;
import com.tmax.eTest.Report.dto.triton.TritonResponseDTO;
import com.tmax.eTest.Report.util.LRSAPIManager;
import com.tmax.eTest.Report.util.StateAndProbProcess;
import com.tmax.eTest.Report.util.TritonAPIManager;
import com.tmax.eTest.Report.util.UKScoreCalculator;
import com.tmax.eTest.Test.model.UkMaster;
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
	StateAndProbProcess stateAndProbProcess;
	@Autowired
	UKScoreCalculator scoreCalculator;

	@Autowired
	ProblemRepository problemRepo;
	@Autowired
	UserKnowledgeRepository userKnowledgeRepo;
	@Autowired
	UserEmbeddingRepository userEmbeddingRepo;

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	public MiniTestResultDTO getMiniTestResult(String userId) {
		MiniTestResultDTO result = new MiniTestResultDTO();
		result.initForDummy();

		// Mini Test 관련 문제 풀이 정보 획득.
		List<StatementDTO> miniTestRes = getMiniTestResultInLRS(userId);
		List<Problem> probInfos = getProblemInfos(miniTestRes);
		Map<String, List<Object>> probInfoForTriton = stateAndProbProcess.makeInfoForTriton(miniTestRes, probInfos);
		TritonResponseDTO tritonResponse = getUnderstandingScoreInTriton(probInfoForTriton);

		TritonDataDTO embeddingData = null;
		TritonDataDTO masteryData = null;

		if(tritonResponse != null)
		{
			for (TritonDataDTO dto : tritonResponse.getOutputs()) {
				if (dto.getName().equals("Embeddings")) {
					embeddingData = dto;
				} else if (dto.getName().equals("Mastery")) {
					masteryData = dto;
				}
			}
	
			int diagQuestionInfo[] = stateAndProbProcess.calculateDiagQuestionInfo(miniTestRes);
			result.setDiagnosisQuestionInfo(diagQuestionInfo);
	
			if (embeddingData != null && masteryData != null) {
				Map<Integer, UkMaster> usedUkMap =stateAndProbProcess.makeUsedUkMap(probInfos);
	
				Map<Integer, Float> ukScoreMap = scoreCalculator.makeUKScoreMap(masteryData);
				List<List<String>> partScoreList = scoreCalculator.makePartScore(usedUkMap, ukScoreMap);
				List<List<String>> weakPartDetail = scoreCalculator.makeWeakPartDetail(usedUkMap, ukScoreMap, partScoreList);
	
				result.setPartUnderstanding(partScoreList);
				result.setWeakPartDetail(weakPartDetail);
	
				float avg = 0;
				for (List<String> part : partScoreList) {
					avg += Float.parseFloat(part.get(2));
				}
	
				result.setScore(Math.round(avg / partScoreList.size()));
	
				saveUserUKInfo(userId, ukScoreMap);
			}
		}
		return result;
	}


	private List<StatementDTO> getMiniTestResultInLRS(String userID) {
		List<StatementDTO> result = new ArrayList<>();
		GetStatementInfoDTO statementInput = new GetStatementInfoDTO();
		statementInput.pushUserId(userID);
		statementInput.pushSourceType("mini_test_question");
		statementInput.pushActionType("submit");

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

	private TritonResponseDTO getUnderstandingScoreInTriton(Map<String, List<Object>> probInfoForTriton) {
		// first process : 문제별 PK 얻어오기.

		TritonRequestDTO tritonReq = new TritonRequestDTO();

		tritonReq.initDefault();

		tritonReq.pushInputData("UKList", "INT32",
				probInfoForTriton.get(stateAndProbProcess.UK_LIST_KEY));
		tritonReq.pushInputData("IsCorrectList", "INT32",
				probInfoForTriton.get(stateAndProbProcess.IS_CORRECT_LIST_KEY));
		tritonReq.pushInputData("DifficultyList", "INT32",
				probInfoForTriton.get(stateAndProbProcess.DIFF_LIST_KEY));

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
