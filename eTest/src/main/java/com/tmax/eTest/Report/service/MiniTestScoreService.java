package com.tmax.eTest.Report.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Report.dto.triton.TritonDataDTO;
import com.tmax.eTest.Report.dto.triton.TritonResponseDTO;
import com.tmax.eTest.Report.exception.ReportBadRequestException;
import com.tmax.eTest.Report.util.SNDCalculator;
import com.tmax.eTest.Report.util.StateAndProbProcess;
import com.tmax.eTest.Report.util.TritonAPIManager;
import com.tmax.eTest.Report.util.UKScoreCalculator;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.report.MinitestReport;
import com.tmax.eTest.Common.model.report.MinitestReportKey;
import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.Common.model.user.UserEmbedding;
import com.tmax.eTest.Common.model.user.UserKnowledge;
import com.tmax.eTest.Common.repository.report.MinitestReportRepo;
import com.tmax.eTest.Test.repository.UserEmbeddingRepository;
import com.tmax.eTest.Test.repository.UserKnowledgeRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
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

	public boolean saveMiniTestResult(String userId, String probSetId) {
		boolean result = true;
		// Mini Test 관련 문제 풀이 정보 획득.
		List<StatementDTO> miniTestRes = getMiniTestResultInLRS(userId, probSetId);
		List<Problem> probInfos = getProblemInfos(miniTestRes);
		Map<String, List<Object>> probInfoForTriton = stateAndProbProcess.makeInfoForTriton(miniTestRes, probInfos);
		TritonResponseDTO tritonResponse = tritonAPIManager.getUnderstandingScoreInTriton(probInfoForTriton);
		TritonDataDTO embeddingData = null;
		TritonDataDTO masteryData = null;
		
		List<List<String>> diagQuestionInfo = stateAndProbProcess.calculateDiagQuestionInfo(miniTestRes);
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
				Map<Integer, Float> ukScoreMap = scoreCalculator.makeUKScoreMap(masteryData);
				float ukModiRatio = 1.5f, ukModiDif = 0.1f;
				ukScoreMap.forEach((ukUuid, score) -> {
					float modScore = score*ukModiRatio - ukModiDif;
					modScore = (modScore > 1)? 1.f : (modScore <= 0.05) ? 0.05f : modScore;
					ukScoreMap.put(ukUuid, modScore);
				});
				
				// 문제 주제 쪽으로 uk 정보 변경.
				Map<String, List<List<String>>> partUkDetail = scoreCalculator.makeThemeInfo(ukScoreMap, probInfos);
				int ukAvgScore = Math.round(scoreCalculator.makeAllThemeAvg(partUkDetail));
				
				int setNum = 0;
				if(probInfos.size() > 0)
					setNum = 1;//probInfos.get(0).getTestInfo().getSetNum(); jinhyung edit
				
				saveUserUKInfo(userId, masteryData.getData().toString());
				saveMinitestReport(userId, probSetId, ukAvgScore, diagQuestionInfo, setNum, partUkDetail);
			}
		}
		
		return result;
	}
	
	public boolean deleteMiniTestResult(String id, String probSetId) throws Exception
	{
		boolean result = true;
		
		Optional<MinitestReport> reportOpt = minitestReportRepo.findById(probSetId);
		
		if(reportOpt.isPresent())
		{
			MinitestReport report = reportOpt.get();
			log.info(id+" "+probSetId);
			if(report.getUserUuid().equals(id))
				minitestReportRepo.deleteById(probSetId);
			else
				throw new ReportBadRequestException(
					"Report's userID and sended userId are not equals in deleteMiniTestResult.");
		}
		else
			throw new ReportBadRequestException("ProbSetId is not available in deleteMiniTestResult. "+probSetId);

		return result;
	}
	
	private void saveMinitestReport(
			String id, 
			String probSetId,
			float ukAvgScore, 
			List<List<String>> diagQuestionInfo,
			int setNum,
			Map<String, List<List<String>>> partUkDetail)
	{
		JSONObject partUkJson = new JSONObject(partUkDetail);
		MinitestReport miniReport = MinitestReport.builder()
			.minitestId(probSetId)
			.userUuid(id)
			.avgUkMastery((float) ukAvgScore)
			.correctNum(diagQuestionInfo.get(0).size())
			.wrongNum(diagQuestionInfo.get(1).size())
			.dunnoNum(diagQuestionInfo.get(2).size())
			.setNum(setNum)
			.minitestDate(Timestamp.valueOf(LocalDateTime.now()))
			.minitestUkMastery(partUkJson.toJSONString())
			.build();
		
		minitestReportRepo.save(miniReport);
	}


	private List<StatementDTO> getMiniTestResultInLRS(String userID, String probSetId) {
		
		GetStatementInfoDTO statementInput = new GetStatementInfoDTO();
		statementInput.pushUserId(userID);
		statementInput.pushSourceType("mini_test_question");
		statementInput.pushActionType("submit");

		List<StatementDTO> result = new ArrayList<>();
		List<StatementDTO> stateResult = new ArrayList<>();
		
		Map<String, Integer> isIDExist = new HashMap<>();
		
		if(probSetId != null)
			statementInput.pushExtensionStr(probSetId);

		try {
			result = lrsAPIManager.getStatementList(statementInput);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.info("Parse fail in getMiniTestResultInLRS : "+ statementInput.toString());
		}
		
	
		for(StatementDTO state : stateResult)
		{
			String sourceID = state.getSourceId();
			
			if(isIDExist.get(sourceID) != null)
			{
				StatementDTO beforeState = result.get(isIDExist.get(sourceID));
				String beforeTimestamp = beforeState.getTimestamp();
				String recentTimestamp = state.getTimestamp();
				
				// 최신 것을 기준으로.
				if(beforeTimestamp.compareTo(recentTimestamp) < 0)
				{
					result.set(isIDExist.get(sourceID), state);
				}
			}
			else
			{
				result.add(state);
				isIDExist.put(sourceID, result.size()-1);
			}
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
			} catch (NumberFormatException e) {
				log.info(
					"Wrong number format in getProblemInfos. id : " + dto.getSourceId() + " error!");
			}
		}
		probList = problemRepo.findAllById(probIdList);
		// problem 관련 정보를 가공하여 TritonInput 화.
		return probList;
	}

	
	
	private void saveUserUKInfo(String userId, String ukMasteryStr) {

		
		UserKnowledge userKnowledge = new UserKnowledge();
		userKnowledge.setUserUuid(userId);
		userKnowledge.setUkMastery(ukMasteryStr);
		userKnowledge.setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));


		try {
			userKnowledgeRepo.save(userKnowledge);
		} catch (IllegalArgumentException e) {
			log.info("Illegal Argument in save user UK Info. " + userKnowledge.toString());
		}
	}	

}
