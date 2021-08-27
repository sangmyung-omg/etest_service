 package com.tmax.eTest.Report.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Report.dto.PartUnderstandingDTO;
import com.tmax.eTest.Report.dto.triton.TritonDataDTO;
import com.tmax.eTest.Report.dto.triton.TritonResponseDTO;
import com.tmax.eTest.Report.exception.ReportBadRequestException;
import com.tmax.eTest.Report.util.RuleBaseScoreCalculator;
import com.tmax.eTest.Report.util.SNDCalculator;
import com.tmax.eTest.Report.util.DiagnosisComment;
import com.tmax.eTest.Report.util.DiagnosisRecommend;
import com.tmax.eTest.Report.util.StateAndProbProcess;
import com.tmax.eTest.Report.util.TritonAPIManager;
import com.tmax.eTest.Report.util.UKScoreCalculator;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.Common.repository.report.DiagnosisReportRepo;
import com.tmax.eTest.Test.repository.UserKnowledgeRepository;

@Service
public class DiagnosisReportService {

	@Autowired
	LRSAPIManager lrsAPIManager;
	@Autowired
	TritonAPIManager tritonAPIManager;
	
	@Autowired
	ProblemRepository problemRepo;
	@Autowired
	UserKnowledgeRepository userKnowledgeRepo;
	@Autowired
	DiagnosisReportRepo diagnosisReportRepo;
	
	@Autowired
	StateAndProbProcess stateAndProbProcess;
	@Autowired
	RuleBaseScoreCalculator ruleBaseScoreCalculator;
	@Autowired
	UKScoreCalculator ukScoreCalculator;
	@Autowired
	DiagnosisComment commentGenerator;
	@Autowired
	SNDCalculator sndCalculator;
	@Autowired
	DiagnosisRecommend recommendGenerator;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	public boolean saveDiagnosisResult(
			String userId, 
			String probSetId) throws Exception
	{
		boolean result = true;
		
		// 1. Get Statement Info
		List<StatementDTO> diagnosisProbStatements = getStatementDiagnosisProb(userId, probSetId);
		
		// 2. get Problem List from Statement Info
		List<Problem> probList = getProblemInfos(diagnosisProbStatements);
		
		// 3. get Problem Answer Info from Problem Info + Statement Info
		List<Pair<Problem, Integer>> probAndUserChoice = getProblemAndChoiceInfos(probList, diagnosisProbStatements);
		
		// 4. get Score Map from Problem Answer Info
		Map<String, Integer> scoreMap = ruleBaseScoreCalculator.probDivideAndCalculateScoresV2(probAndUserChoice);

		// 5. get recommend info
		List<JsonArray> recommendInfo = recommendGenerator.getRecommendLists(probAndUserChoice, scoreMap);
		
		// Rule Based 점수들 산출
		Map<String, List<Object>> probInfoForTriton = stateAndProbProcess.makeInfoForTriton(diagnosisProbStatements, probList);
		TritonResponseDTO tritonResponse = tritonAPIManager.getUnderstandingScoreInTriton(probInfoForTriton);
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
	
			if (embeddingData != null && masteryData != null) {
				Map<Integer, UkMaster> usedUkMap =stateAndProbProcess.makeUsedUkMap(probList);
				Map<Integer, Float> ukScoreMap = ukScoreCalculator.makeUKScoreMap(masteryData);
				
				//[파트이름, 스코어 등급(A~F), 스코어 점수]
				List<List<String>> partScoreList = ukScoreCalculator.makePartScore(usedUkMap, ukScoreMap);
				
				Collections.sort(partScoreList, new Comparator<List<String>>() {
					@Override
					public int compare(List<String> o1, List<String> o2) {
						// TODO Auto-generated method stub
						return o1.get(2).compareTo(o2.get(2));
					}
				});
				
				float partAvgScore = 0;
				
				for(int i = 0; i < partScoreList.size(); i++)
				{
					List<String> partScoreInfo = partScoreList.get(i);
					int partScore = Integer.parseInt(partScoreInfo.get(2));
					partAvgScore += partScore;
				}
				
				if(partScoreList.size() > 0)
					partAvgScore /= partScoreList.size();
				saveDiagnosisReport(userId, probSetId, scoreMap, recommendInfo, partAvgScore);
				
			}
		}
		
		return result;
	}

	
	public PartUnderstandingDTO getPartInfo(String id, String partName) throws Exception
	{
		PartUnderstandingDTO res = new PartUnderstandingDTO();
		
		List<StatementDTO> diagnosisProbStatements = getStatementDiagnosisProb(id, null);
		List<Problem> probList = getProblemInfos(diagnosisProbStatements);
		Map<String, List<Object>> probInfoForTriton = stateAndProbProcess.makeInfoForTriton(diagnosisProbStatements, probList);
		TritonResponseDTO tritonResponse = tritonAPIManager.getUnderstandingScoreInTriton(probInfoForTriton);
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
	
			if (embeddingData != null && masteryData != null) {
				Map<Integer, UkMaster> usedUkMap =stateAndProbProcess.makeUsedUkMap(probList);
				Map<Integer, Float> ukScoreMap = ukScoreCalculator.makeUKScoreMap(masteryData);
				
				//[파트이름, 스코어 등급(A~F), 스코어 점수]
				List<List<String>> partScoreList = ukScoreCalculator.makePartScore(usedUkMap, ukScoreMap);
				
				for(List<String> partScoreInfo : partScoreList)
				{
					if(partScoreInfo.get(0).equals(partName))
					{
						res.setPoint(Integer.parseInt(partScoreInfo.get(2)));
						break;
					}
				}
				
				usedUkMap.forEach((ukId, ukMaster) -> {
					if(ukMaster.getPart().equals(partName))
					{
						res.pushUKUnderstanding(ukMaster.getUkName(), 
								Float.valueOf(ukScoreMap.get(ukId)*100).intValue());
					}
					
				});

			}
		}
		else
			throw new ReportBadRequestException("Triton Result is Null. Check Input or Triton Server.");
		
		return res;
	}
	
	private void saveDiagnosisReport(
			String id,
			String probSetId,
			Map<String, Integer> scoreMap, 
			List<JsonArray> recommendLists,
			float avgUkMastery)
	{
		

		int investItemNum = 0, stockRatio = 0;
		
		int[] investItemNumList = {2, 5, 7, 10};
		int[] investStockRatioList = {5, 20, 40, 55};
		
		if(scoreMap.get(RuleBaseScoreCalculator.STOCK_NUM_ANS) != null
				&& scoreMap.get(RuleBaseScoreCalculator.STOCK_NUM_ANS) < investItemNumList.length)
			investItemNum = investItemNumList[scoreMap.get(RuleBaseScoreCalculator.STOCK_NUM_ANS)];
		
		if(scoreMap.get(RuleBaseScoreCalculator.STOCK_RATIO_ANS) != null
				&& scoreMap.get(RuleBaseScoreCalculator.STOCK_RATIO_ANS) < investStockRatioList.length)
			investItemNum = investStockRatioList[scoreMap.get(RuleBaseScoreCalculator.STOCK_RATIO_ANS)];
		
		DiagnosisReport report = DiagnosisReport.builder()
				.diagnosisId((probSetId == null)
						? UUID.randomUUID().toString()
						: probSetId)
				.userUuid(id)
				.giScore(scoreMap.get(RuleBaseScoreCalculator.GI_SCORE_KEY))
				.riskScore(scoreMap.get(RuleBaseScoreCalculator.RISK_SCORE))
				.riskProfileScore(scoreMap.get(RuleBaseScoreCalculator.RISK_PROFILE_SCORE))
				.riskTracingScore(scoreMap.get(RuleBaseScoreCalculator.RISK_TRACING_SCORE))
				.riskLevelScore(scoreMap.get(RuleBaseScoreCalculator.RISK_PROFILE_LEVEL))
				.riskCapaScore(scoreMap.get(RuleBaseScoreCalculator.RISK_PROFILE_CAPA))
				.investScore(scoreMap.get(RuleBaseScoreCalculator.INVEST_SCORE))
				.investProfileScore(scoreMap.get(RuleBaseScoreCalculator.INVEST_PROFILE))
				.investTracingScore(scoreMap.get(RuleBaseScoreCalculator.INVEST_TRACING))
				.knowledgeScore(scoreMap.get(RuleBaseScoreCalculator.KNOWLEDGE_SCORE))
				.knowledgeCommonScore(scoreMap.get(RuleBaseScoreCalculator.KNOWLEDGE_COMMON))
				.knowledgeChangeScore(scoreMap.get(RuleBaseScoreCalculator.KNOWLEDGE_CHANGE))
				.knowledgeSellScore(scoreMap.get(RuleBaseScoreCalculator.KNOWLEDGE_SELL))
				.knowledgeTypeScore(scoreMap.get(RuleBaseScoreCalculator.KNOWLEDGE_TYPE))
				.recommendBasicList(recommendLists.get(0).toString())
				.recommendAdvancedList(recommendLists.get(1).toString())
				.recommendTypeList(recommendLists.get(2).toString())
				.avgUkMastery(avgUkMastery)
				.userMbti("XXXX")
				.investItemNum(investItemNum)
				.stockRatio(stockRatio)
				.diagnosisDate(Timestamp.valueOf(LocalDateTime.now()))
				.build();

		
		// score 관련 저장
		
		logger.info(report.toString());
		diagnosisReportRepo.save(report);
	}
	
	private List<Pair<Problem, Integer>> getProblemAndChoiceInfos(
			List<Problem> probList,
			List<StatementDTO> statementList)
	{
		List<Pair<Problem, Integer>> result = new ArrayList<>();
		Map<Integer, Integer> probChoiceMap = new HashMap<>();
		
		for(StatementDTO statement : statementList)
		{
			int probId = -1, answerNum = -1;
			
			try {
				probId = Integer.parseInt(statement.getSourceId());
				answerNum = Integer.parseInt(statement.getUserAnswer());
			}
			catch(Exception e)
			{
				logger.info("getProbAndChoiceInfos : "+e.toString());
			}
			
			if(probId!= -1 && answerNum != -1)
				probChoiceMap.put(probId, answerNum);
		}
		
		for(Problem prob : probList)
		{
			Integer choice = probChoiceMap.get(prob.getProbID());
			
			if(choice != null)
				result.add(Pair.of(prob, choice));
		}
		
		
		return result;
	}
	
	
	private List<Problem> getProblemInfos(List<StatementDTO> statementList) {
		List<Integer> probIdList = new ArrayList<>();
		List<Problem> probList = new ArrayList<>();

		for (StatementDTO dto : statementList) {
			try {
				int probId = Integer.parseInt(dto.getSourceId());
				probIdList.add(probId);
			} catch (Exception e) {
				logger.info("getProblemInfos : " + e.toString() + " id : " + dto.getSourceId() + " error!");
			}
		}
		probList = problemRepo.findAllById(probIdList);

		// problem 관련 정보를 가공하여 TritonInput 화.
		return probList;
	}
	

	
	private List<StatementDTO> getStatementDiagnosisProb(String id, String probSetId) throws Exception
	{
		GetStatementInfoDTO getStateInfo = new GetStatementInfoDTO();
		
	
		getStateInfo.pushUserId(id);

		getStateInfo.pushSourceType("diagnosis");	
		getStateInfo.pushSourceType("diagnosis_pattern");		
		getStateInfo.pushActionType("submit");
		
		if(probSetId != null)
			getStateInfo.pushExtensionStr(probSetId);
		
		List<StatementDTO> result = new ArrayList<>();
		List<StatementDTO> stateResult;
		Map<String, Integer> isIDExist = new HashMap<>();
		
		try
		{
			stateResult = lrsAPIManager.getStatementList(getStateInfo); 
		}
		catch(Exception e)
		{
			throw new ReportBadRequestException("Exception in Diagnosis Report, get statement part.", e);
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
		
		logger.info(result.size()+"    "+result.toString());
		
		
		return result;
	}
	
}