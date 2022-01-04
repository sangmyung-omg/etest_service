 package com.tmax.eTest.Report.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Report.exception.ReportBadRequestException;
import com.tmax.eTest.Report.util.diagnosis.DiagnosisRecommend;
import com.tmax.eTest.Report.util.diagnosis.RuleBaseScoreCalculator;
import com.tmax.eTest.Report.util.diagnosis.DiagnosisUtil.InvestProfile;
import com.tmax.eTest.Report.util.diagnosis.DiagnosisUtil.InvestTracing;
import com.tmax.eTest.Report.util.diagnosis.DiagnosisUtil.KnowledgeSection;
import com.tmax.eTest.Report.util.diagnosis.DiagnosisUtil.KnowledgeSubSection;
import com.tmax.eTest.Report.util.diagnosis.DiagnosisUtil.RiskProfile;
import com.tmax.eTest.Report.util.diagnosis.DiagnosisUtil.RiskTracing;
import com.tmax.eTest.Report.util.diagnosis.DiagnosisUtil.ScoreKey;
import com.tmax.eTest.Report.util.diagnosis.DiagnosisUtil.TendencySection;
import com.tmax.eTest.Report.util.StateAndProbProcess;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Common.repository.report.DiagnosisReportRepo;
import com.tmax.eTest.Common.repository.user.UserMasterRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DiagnosisReportService {

	@Autowired
	LRSAPIManager lrsAPIManager;
	
	@Autowired
	ProblemRepository problemRepo;
	@Autowired
	DiagnosisReportRepo diagnosisReportRepo;
	@Autowired
	UserMasterRepo userMasterRepo;
	
	@Autowired
	StateAndProbProcess stateAndProbProcess;
	@Autowired
	RuleBaseScoreCalculator ruleBaseScoreCalculator;
	@Autowired
	DiagnosisRecommend recommendGenerator;
	
	public boolean saveDiagnosisResult(
			String id, 
			String probSetId) throws Exception
	{
		String userId = id;
		boolean result = true;
		if(diagnosisReportRepo.existsById(probSetId))
		{
			log.info("Have report in saveDiagnosisResult. Save process stop.");
			return result;
		}
		
		// 1. Get Statement Info
		List<StatementDTO> diagnosisProbStatements = getStatementDiagnosisProb(probSetId);
		
		if(userId == null)
			if(diagnosisProbStatements.size() <= 0)
				throw new ReportBadRequestException("Nonmember's ProbSetId is not available. "+probSetId);
			else
			{
				userId = diagnosisProbStatements.get(0).getUserId();
				log.info("Nonmember's id : "+userId);
			}
		
		
			
		// 2. get Problem List from Statement Info
		List<Problem> probList = getProblemInfos(diagnosisProbStatements);
		
		// 3. get Problem Answer Info from Problem Info + Statement Info
		List<Pair<Problem, Integer>> probAndUserChoice = getProblemAndChoiceInfos(probList, diagnosisProbStatements);
		
		// 4. get Score Map from Problem Answer Info
		Map<String, Integer> scoreMap = ruleBaseScoreCalculator.probDivideAndCalculateScoresV2(probAndUserChoice);

		// 5. get recommend info
		List<JsonArray> recommendInfo = recommendGenerator.getRecommendLists(probAndUserChoice, scoreMap);
		
		saveDiagnosisReport(userId, probSetId, scoreMap, recommendInfo, 0);

		return result;
	}
	
	public boolean deleteDiagnosisReport(String id, String probSetId) throws ReportBadRequestException
	{
		boolean result = true;
		
		Optional<DiagnosisReport> reportOpt = diagnosisReportRepo.findById(probSetId);
		
		if(reportOpt.isPresent())
		{
			DiagnosisReport report = reportOpt.get();
			
			if(report.getUserUuid().equals(id))
				diagnosisReportRepo.deleteById(probSetId);
			else
				throw new ReportBadRequestException(
					"Report's userID and sended userId are not equals in deleteDiagnosisReport.");
		}
		else
		{
			throw new ReportBadRequestException("ProbSetId is not available in deleteDiagnosisReport. "+probSetId);
		}
		
		return result;
	}

	private void saveDiagnosisReport(
			String id,
			String probSetId,
			Map<String, Integer> scoreMap, 
			List<JsonArray> recommendLists,
			float avgUkMastery)
	{
		
		int investItemNum = 0, stockRatio = 0, investPeriod = 0;
		
		int[] investItemNumList = {16, 11, 5, 2};
		int[] investStockRatioList = {20, 40, 60, 80};
		
		if(scoreMap.get(RiskTracing.STOCK_NUM.toString()) != null
				&& scoreMap.get(RiskTracing.STOCK_NUM.toString()) < investItemNumList.length)
			investItemNum = investItemNumList[scoreMap.get(RiskTracing.STOCK_NUM.toString())];
		
		if(scoreMap.get(RiskTracing.STOCK_RATIO.toString()) != null
				&& scoreMap.get(RiskTracing.STOCK_RATIO.toString()) < investStockRatioList.length)
			stockRatio = investStockRatioList[scoreMap.get(RiskTracing.STOCK_RATIO.toString())];
		
		if(scoreMap.get(RiskTracing.STOCK_PERIOD.toString()) != null)
			investPeriod = scoreMap.get(RiskTracing.STOCK_PERIOD.toString());
		
		DiagnosisReport report = DiagnosisReport.builder()
				.diagnosisId((probSetId == null)
						? UUID.randomUUID().toString()
						: probSetId)
				.userUuid(id)
				.giScore(scoreMap.get(ScoreKey.GI.toString()))
				.riskScore(scoreMap.get(ScoreKey.RISK.toString()))
				.riskProfileScore(scoreMap.get(ScoreKey.RISK_PROFILE.toString()))
				.riskLevelScore(scoreMap.get(RiskProfile.LEVEL.toString()))
				.riskCapaScore(scoreMap.get(RiskProfile.CAPACITY.toString()))
				.riskTracingScore(scoreMap.get(ScoreKey.RISK_TRACING.toString()))
				.riskInvestPeriodScore(scoreMap.get(RiskTracing.STOCK_PERIOD.toString()))
				.riskStockRatioScore(scoreMap.get(RiskTracing.STOCK_RATIO.toString()))
				.riskStockNumScore(scoreMap.get(RiskTracing.STOCK_NUM.toString()))
				.riskStockPreferScore(scoreMap.get(RiskTracing.STOCK_PREFER.toString()))
				.investScore(scoreMap.get(ScoreKey.INVEST.toString()))
				.investProfileScore(scoreMap.get(TendencySection.INVEST_PROFILE.toString()))
				.investAnchorScore(scoreMap.get(InvestProfile.BIAS_ANCHOR.toString()))				/// 임시
				.investSelfScore(scoreMap.get(InvestProfile.BIAS_SELF.toString()))
				.investLossScore(scoreMap.get(InvestProfile.BIAS_LOSS.toString()))
				.investConfirmScore(scoreMap.get(InvestProfile.BIAS_CONFIRM.toString()))
				.investCrownScore(scoreMap.get(InvestProfile.BIAS_CROWN.toString()))
				.investTracingScore(scoreMap.get(TendencySection.INVEST_TRACING.toString()))
				.investMethodScore(scoreMap.get(InvestTracing.RULE_METHOD.toString()))
				.investSellScore(scoreMap.get(InvestTracing.RULE_SELL.toString()))
				.investPortfolioScore(scoreMap.get(InvestTracing.RULE_PORTFOLIO.toString()))
				.investInfoScore(scoreMap.get(InvestTracing.RULE_INFO.toString()))
				.knowledgeScore(scoreMap.get(ScoreKey.KNOWLEDGE.toString()))
				.knowledgeCommonScore(scoreMap.get(KnowledgeSection.BASIC.toString()))
				.knowledgeCommonBasic(scoreMap.get(KnowledgeSubSection.BASIC.toStringForScoreMap()))
				.knowledgeCommonProfit(scoreMap.get(KnowledgeSubSection.PROFIT_GUARANTEED.toStringForScoreMap()))
				.knowledgeCommonRule(scoreMap.get(KnowledgeSubSection.INVEST_RULE.toStringForScoreMap()))
				.knowledgeChangeScore(scoreMap.get(KnowledgeSection.PRICE_CHANGE.toString()))
				.knowledgeSellScore(scoreMap.get(KnowledgeSection.SELL_WAY.toString()))
				.knowledgeTypeScore(scoreMap.get(KnowledgeSection.TYPE_SELECT.toString()))
				.recommendBasicList(recommendLists.get(0).toString())
				.recommendAdvancedList(recommendLists.get(1).toString())
				.recommendTypeList(recommendLists.get(2).toString())
				.avgUkMastery(avgUkMastery)
				.userMbti("XXXX")
				.investItemNum(investItemNum)
				.stockRatio(stockRatio)
				.investPeriod(investPeriod)
				.diagnosisDate(Timestamp.valueOf(LocalDateTime.now()))
				.build();

		
		// score 관련 저장
		log.info(report.toString());
		diagnosisReportRepo.save(report);
	}
	
	private List<Pair<Problem, Integer>> getProblemAndChoiceInfos(
			List<Problem> probList,
			List<StatementDTO> statementList)
	{
		Map<Integer, Integer> probChoiceMap = statementList.stream()
			.filter(StatementDTO::isInvalidProblemData)
			.collect(Collectors.toMap(
					state -> Integer.parseInt(state.getSourceId()), 
					state -> Integer.parseInt(state.getUserAnswer())));
		
		List<Pair<Problem, Integer>> result = new ArrayList<>();
				
		for(Problem prob : probList)
		{
			Integer choice = probChoiceMap.get(prob.getProbID());
			
			if(choice != null)
				result.add(Pair.of(prob, choice));
		}
		
		
		return result;
	}
	
	
	private List<Problem> getProblemInfos(List<StatementDTO> statementList) {
		List<Integer> probIdList = statementList.stream()
				.filter(StatementDTO::isInvalidProblemData)
				.map(state -> Integer.parseInt(state.getSourceId()))
				.collect(Collectors.toList());
		
		return problemRepo.findAllById(probIdList);
	}
	

	
	private List<StatementDTO> getStatementDiagnosisProb(
			//String id, 
			String probSetId)
	{
		GetStatementInfoDTO getStateInfo = new GetStatementInfoDTO();

		getStateInfo.pushSourceType("diagnosis");	
		getStateInfo.pushSourceType("diagnosis_pattern");		
		getStateInfo.pushActionType("submit");
		
		if(probSetId != null)
			getStateInfo.pushExtensionStr(probSetId);
		
		List<StatementDTO> result = new ArrayList<>();
		List<StatementDTO> stateResult = new ArrayList<>();
		Map<String, Integer> isIDExist = new HashMap<>();
		
		try {
			stateResult = lrsAPIManager.getStatementList(getStateInfo);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.info("Parse fail in getStatementDiagnosisProb : "+ getStateInfo.toString());
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
	
}
