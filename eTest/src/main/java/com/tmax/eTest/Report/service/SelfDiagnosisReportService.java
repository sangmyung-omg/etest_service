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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Report.dto.DiagnosisResultDTO;
import com.tmax.eTest.Report.dto.PartUnderstandingDTO;
import com.tmax.eTest.Report.dto.triton.TritonDataDTO;
import com.tmax.eTest.Report.dto.triton.TritonResponseDTO;
import com.tmax.eTest.Report.exception.ReportBadRequestException;
import com.tmax.eTest.Report.util.RuleBaseScoreCalculator;
import com.tmax.eTest.Report.util.SNDCalculator;
import com.tmax.eTest.Report.util.SelfDiagnosisComment;
import com.tmax.eTest.Report.util.StateAndProbProcess;
import com.tmax.eTest.Report.util.TritonAPIManager;
import com.tmax.eTest.Report.util.UKScoreCalculator;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.Common.repository.report.DiagnosisReportRepo;
import com.tmax.eTest.Test.repository.UserKnowledgeRepository;

@Service
public class SelfDiagnosisReportService {

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
	SelfDiagnosisComment commentGenerator;
	@Autowired
	SNDCalculator sndCalculator;
	
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
				saveDiagnosisReport(userId, probSetId, scoreMap, partAvgScore);
				
			}
		}
		
		return result;
	}
	
	public DiagnosisResultDTO calculateDiagnosisResult(
			String userId, 
			String probSetId) throws Exception
	{
		DiagnosisResultDTO result = new DiagnosisResultDTO();
		
		// 1. Get Statement Info
		List<StatementDTO> diagnosisProbStatements = getStatementDiagnosisProb(userId, probSetId);
		
		// 2. get Problem List from Statement Info
		List<Problem> probList = getProblemInfos(diagnosisProbStatements);
		
		// 3. get Problem Answer Info from Problem Info + Statement Info
		List<Pair<Problem, Integer>> probAndUserChoice = getProblemAndChoiceInfos(probList, diagnosisProbStatements);
		
		// 4. get Score Map from Problem Answer Info
		Map<String, Integer> scoreMap = ruleBaseScoreCalculator.probDivideAndCalculateScores(probAndUserChoice);
		
		// 5. get Comment From Score Map
		Map<String, List<String>> commentMap = commentGenerator.getTotalComments(scoreMap);
			
		// 6. set Main Score Info (RISK_FIDELITY, DECISION_MAKING, INVESTMENT_KNOWLEDGE SCORE)
		Map<String, Integer> mainScoreInfo = new HashMap<>();
		mainScoreInfo.put(RuleBaseScoreCalculator.RISK_SCORE, 
				scoreMap.get(RuleBaseScoreCalculator.RISK_SCORE));
		mainScoreInfo.put(RuleBaseScoreCalculator.INVEST_SCORE, 
				scoreMap.get(RuleBaseScoreCalculator.INVEST_SCORE));
		mainScoreInfo.put(RuleBaseScoreCalculator.KNOWLEDGE_SCORE, 
				scoreMap.get(RuleBaseScoreCalculator.KNOWLEDGE_SCORE));

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
				saveDiagnosisReport(userId, probSetId, scoreMap, partAvgScore);
				
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
			float avgUkMastery)
	{
		DiagnosisReport report = new DiagnosisReport();

		int investItemNum = 0, stockRatio = 0;
		
		int[] investItemNumList = {2, 5, 7, 10};
		int[] investStockRatioList = {5, 20, 40, 55};
		
		if(scoreMap.get(RuleBaseScoreCalculator.STOCK_NUM_ANS) != null
				&& scoreMap.get(RuleBaseScoreCalculator.STOCK_NUM_ANS) < investItemNumList.length)
			investItemNum = investItemNumList[scoreMap.get(RuleBaseScoreCalculator.STOCK_NUM_ANS)];
		
		if(scoreMap.get(RuleBaseScoreCalculator.STOCK_RATIO_ANS) != null
				&& scoreMap.get(RuleBaseScoreCalculator.STOCK_RATIO_ANS) < investStockRatioList.length)
			investItemNum = investStockRatioList[scoreMap.get(RuleBaseScoreCalculator.STOCK_RATIO_ANS)];
		
		
		report.setDiagnosisId((probSetId == null)
				? UUID.randomUUID().toString()
				: probSetId);
		report.setUserUuid(id);
		
		// score 관련 저장
		report.setGiScore((float)scoreMap.get(RuleBaseScoreCalculator.GI_SCORE_KEY));

		report.setRiskScore(scoreMap.get(RuleBaseScoreCalculator.RISK_SCORE));
		report.setRiskProfileScore(scoreMap.get(RuleBaseScoreCalculator.RISK_PROFILE_SCORE));
		report.setRiskTracingScore(scoreMap.get(RuleBaseScoreCalculator.RISK_TRACING_SCORE));
		report.setRiskLevelScore(scoreMap.get(RuleBaseScoreCalculator.RISK_PROFILE_LEVEL));
		report.setRiskCapaScore(scoreMap.get(RuleBaseScoreCalculator.RISK_PROFILE_CAPA));
		
		report.setInvestScore(scoreMap.get(RuleBaseScoreCalculator.INVEST_SCORE));
		report.setInvestProfileScore(scoreMap.get(RuleBaseScoreCalculator.INVEST_PROFILE));
		report.setInvestTracingScore(scoreMap.get(RuleBaseScoreCalculator.INVEST_TRACING));
		
		report.setKnowledgeScore(scoreMap.get(RuleBaseScoreCalculator.KNOWLEDGE_SCORE));
		report.setKnowledgeCommonScore(scoreMap.get(RuleBaseScoreCalculator.KNOWLEDGE_COMMON));
		report.setKnowledgeChangeScore(scoreMap.get(RuleBaseScoreCalculator.KNOWLEDGE_CHANGE));
		report.setKnowledgeSellScore(scoreMap.get(RuleBaseScoreCalculator.KNOWLEDGE_SELL));
		report.setKnowledgeTypeScore(scoreMap.get(RuleBaseScoreCalculator.KNOWLEDGE_TYPE));
		
		report.setAvgUkMastery(avgUkMastery);
		report.setUserMbti("XXXX");
		report.setInvestItemNum(investItemNum);
		report.setStockRatio(stockRatio);
		report.setDiagnosisDate(Timestamp.valueOf(LocalDateTime.now()));
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
	

	
	private List<StatementDTO> getStatementDiagnosisProb(String id, String probSetId)
	{
		GetStatementInfoDTO getStateInfo = new GetStatementInfoDTO();
	
		getStateInfo.pushUserId(id);

		getStateInfo.pushSourceType("diagnosis");	
		getStateInfo.pushSourceType("diagnosis_pattern");		
		getStateInfo.pushActionType("submit");
		
		if(probSetId != null)
			getStateInfo.pushExtensionStr(probSetId);
		
		try
		{
			return lrsAPIManager.getStatementList(getStateInfo);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
}
