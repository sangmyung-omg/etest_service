 package com.tmax.eTest.Report.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.Report.dto.DiagnosisResultDTO;
import com.tmax.eTest.Report.dto.PartUnderstandingDTO;
import com.tmax.eTest.Report.dto.lrs.GetStatementInfoDTO;
import com.tmax.eTest.Report.dto.lrs.StatementDTO;
import com.tmax.eTest.Report.dto.triton.TritonDataDTO;
import com.tmax.eTest.Report.dto.triton.TritonResponseDTO;
import com.tmax.eTest.Report.exception.ReportBadRequestException;
import com.tmax.eTest.Report.util.LRSAPIManager;
import com.tmax.eTest.Report.util.RuleBaseScoreCalculator;
import com.tmax.eTest.Report.util.SNDCalculator;
import com.tmax.eTest.Report.util.SelfDiagnosisComment;
import com.tmax.eTest.Report.util.StateAndProbProcess;
import com.tmax.eTest.Report.util.TritonAPIManager;
import com.tmax.eTest.Report.util.UKScoreCalculator;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.Test.repository.DiagnosisReportRepo;
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
	
	
	public DiagnosisResultDTO calculateDiagnosisResult(String id) throws Exception
	{
		DiagnosisResultDTO result = new DiagnosisResultDTO();
		List<StatementDTO> diagnosisProbStatements = getStatementDiagnosisProb(id);
		List<Problem> probList = getProblemInfos(diagnosisProbStatements);
		List<Pair<Problem, Integer>> probAndUserChoice = getProblemAndChoiceInfos(probList, diagnosisProbStatements);
		Map<String, Integer> scoreMap = ruleBaseScoreCalculator.probDivideAndCalculateScores(probAndUserChoice);
		Map<String, List<String>> commentMap = commentGenerator.getTotalComments(scoreMap);
		
		//result.initForDummy();
				
		Map<String, Integer> partRes = new HashMap<>();
		partRes.put(RuleBaseScoreCalculator.RISK_FIDELITY_SCORE_KEY, 
				scoreMap.get(RuleBaseScoreCalculator.RISK_FIDELITY_SCORE_KEY));
		partRes.put(RuleBaseScoreCalculator.DECISION_MAKING_SCORE_KEY, 
				scoreMap.get(RuleBaseScoreCalculator.DECISION_MAKING_SCORE_KEY));
		partRes.put(RuleBaseScoreCalculator.INVEST_KNOWLEDGE_KEY, 
				scoreMap.get(RuleBaseScoreCalculator.INVEST_KNOWLEDGE_KEY));
		result.setPartDiagnosisResult(partRes);
		result.setGiScore(scoreMap.get(RuleBaseScoreCalculator.GI_SCORE_KEY));
		result.setGiPercentage(sndCalculator.calculateForSelfDiag(
				scoreMap.get(RuleBaseScoreCalculator.GI_SCORE_KEY)));
		
		
		// Comment Setting
		result.setTotalResult(commentMap.get(SelfDiagnosisComment.TOTAL_RES_KEY));
		result.setDecisionMaking(commentMap.get(SelfDiagnosisComment.DECISION_MAKING_KEY));
		result.setInvestKnowledge(commentMap.get(SelfDiagnosisComment.INVEST_KNOWLEDGE_KEY));
		result.setRiskFidelity(commentMap.get(SelfDiagnosisComment.RISK_FID_KEY));
		result.setSimilarTypeInfo(commentMap.get(SelfDiagnosisComment.SIMILAR_TYPE_KEY));
		
		
		
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
					
					if(i >= partScoreList.size() / 2)
						result.pushStrongPartInfo(partScoreInfo.get(0), 
							partScoreInfo.get(0) +" 영역에서 상대적으로 높은 이해도를 갖고 있습니다.", 
							partScore);
					else
						result.pushWeakPartInfo(partScoreInfo.get(0), 
								partScoreInfo.get(0) +"는 더 많은 이해가 필요해 보입니다. AI가 학습 컨텐츠를 추천해드립니다.", 
								partScore);
					
				}
				
				partAvgScore /= partScoreList.size();
				saveDiagnosisReport(id, scoreMap, partAvgScore);
			}
		}
		
		return result;
	}
	

	
	public PartUnderstandingDTO getPartInfo(String id, String partName) throws Exception
	{
		PartUnderstandingDTO res = new PartUnderstandingDTO();
		
		List<StatementDTO> diagnosisProbStatements = getStatementDiagnosisProb(id);
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
	
	private void saveDiagnosisReport(String id, Map<String, Integer> scoreMap, float avgUkMastery)
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
			
		report.setUserUuid(id);
		report.setGiScore((float)scoreMap.get(RuleBaseScoreCalculator.GI_SCORE_KEY));
		report.setRiskScore(scoreMap.get(RuleBaseScoreCalculator.RISK_FIDELITY_SCORE_KEY));
		report.setInvestScore(scoreMap.get(RuleBaseScoreCalculator.DECISION_MAKING_SCORE_KEY));
		report.setKnowledgeScore(scoreMap.get(RuleBaseScoreCalculator.INVEST_KNOWLEDGE_KEY));
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
				probList = problemRepo.findAllById(probIdList);
			} catch (Exception e) {
				logger.info("getProblemInfos : " + e.toString() + " id : " + dto.getSourceId() + " error!");
			}
		}

		// problem 관련 정보를 가공하여 TritonInput 화.
		return probList;
	}
	

	
	private List<StatementDTO> getStatementDiagnosisProb(String id)
	{
		GetStatementInfoDTO getStateInfo = new GetStatementInfoDTO();
	
		getStateInfo.pushUserId(id);

		getStateInfo.pushSourceType("diagnosis");	
		getStateInfo.pushSourceType("diagnosis_pattern");		
		getStateInfo.pushActionType("submit");
		
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
