package com.tmax.eTest.Report.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Report.dto.DiagnosisRecordDetailDTO;
import com.tmax.eTest.Report.dto.DiagnosisRecordMainDTO;
import com.tmax.eTest.Report.exception.ReportBadRequestException;
import com.tmax.eTest.Report.util.SNDCalculator;
import com.tmax.eTest.Report.util.DiagnosisComment;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.report.DiagnosisReportKey;
import com.tmax.eTest.Common.repository.report.DiagnosisReportRepo;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DiagnosisRecordService {

	@Autowired
	LRSAPIManager lrsAPIManager;

	@Autowired
	ProblemRepository problemRepo;
	@Autowired
	DiagnosisReportRepo diagnosisReportRepo;

	@Autowired
	DiagnosisComment commentGenerator;
	@Autowired
	SNDCalculator sndCalculator;

	private final List<String> partNameList = new ArrayList<>(Arrays.asList("risk", "invest", "knowledge"));


	public DiagnosisRecordMainDTO getDiagnosisRecordMain(
			String userId, 
			String probSetId) throws Exception {
		
		DiagnosisRecordMainDTO result = new DiagnosisRecordMainDTO();

		if(probSetId.equals("dummy"))
			result.initForDummy();
		else
		{
			DiagnosisReportKey key = new DiagnosisReportKey();
			key.setDiagnosisId(probSetId);
			
			Optional<DiagnosisReport> reportOpt = diagnosisReportRepo.findById(key);
			
			if(reportOpt.isPresent())
			{
				DiagnosisReport report = reportOpt.get();
				
				Map<String, Integer> percentList = new HashMap<>();
				percentList.put("gi", 
					sndCalculator.calculateForDiagnosis(sndCalculator.GI_IDX ,report.getGiScore()));
				percentList.put("risk",
					sndCalculator.calculateForDiagnosis(sndCalculator.RISK_IDX, report.getRiskScore()));
				percentList.put("invest", 
					sndCalculator.calculateForDiagnosis(sndCalculator.INVEST_IDX, report.getInvestScore()));
				percentList.put("knowledge", 
					sndCalculator.calculateForDiagnosis(sndCalculator.KNOWLEDGE_IDX, report.getKnowledgeScore()));
				
				List<String> similarTypeInfo = commentGenerator.makeSimilarTypeInfo(
						report.getRiskScore(), 
						report.getInvestScore(), 
						report.getKnowledgeScore());
				
				result.pushInfoByReport(
						report, 
						percentList, 
						similarTypeInfo);
			}
			else
				throw new ReportBadRequestException("Problem Set Id is unavailable. "+probSetId);
		}

		return result;
	}
	
	public DiagnosisRecordDetailDTO getDiagnosisRecordDetail(
			String userId, 
			String probSetId,
			String partName) throws Exception {
		DiagnosisRecordDetailDTO result = new DiagnosisRecordDetailDTO();
		
		if(!partNameList.contains(partName))
			throw new ReportBadRequestException("PartName unavailable. "+partName);
		
		if(probSetId.equals("dummy"))
			result.initForDummy();
		else
		{
			DiagnosisReportKey key = new DiagnosisReportKey();
			key.setDiagnosisId(probSetId);
			
			Optional<DiagnosisReport> reportOpt = diagnosisReportRepo.findById(key);
			
			List<StatementDTO> knowledgeProbStatement = getDiagnosisKnowledgeProbInfo(userId, probSetId);
			
			
			if(reportOpt.isPresent())
			{
				DiagnosisReport report = reportOpt.get();
				
				switch(partName)
				{
				case "risk":
					result = makeRiskRecordDetail(report);
					break;
				case "invest":
					result = makeInvestRecordDetail(report);
					break;
				case "knowledge":
					result = makeKnowledgeRecordDetail(report, knowledgeProbStatement);
					break;
				default: // 해당 경우 없음.
					throw new ReportBadRequestException("PartName unavailable. "+partName);
				}
			}
			else
				throw new ReportBadRequestException("Problem Set Id is unavailable. "+probSetId);
		}

		return result;
	}
	
	@SuppressWarnings("unchecked")
	private DiagnosisRecordDetailDTO makeKnowledgeRecordDetail(
			DiagnosisReport report,
			List<StatementDTO> knowledgeProbStatement) throws Exception
	{
		
		Map<String, Object> probInfos = setProbInfoInRecordDTO(knowledgeProbStatement);
		
		Map<String, Object> problemCorrectInfo = (Map<String, Object>) probInfos.get("problemCorrectInfo");
		List<List<String>> problemHighLevelInfo = (List<List<String>>) probInfos.get("problemHighLevelInfo");
		List<List<String>> problemMiddleLevelInfo = (List<List<String>>) probInfos.get("problemMiddleLevelInfo");
		List<List<String>> problemLowLevelInfo = (List<List<String>>) probInfos.get("problemLowLevelInfo");
		
		DiagnosisRecordDetailDTO result = DiagnosisRecordDetailDTO.builder()
				.score(report.getKnowledgeScore())
				.percentage(sndCalculator.calculateForDiagnosis(
						sndCalculator.KNOWLEDGE_IDX, 
						report.getKnowledgeScore()))
				.mainCommentInfo(commentGenerator.makeKnowledgeMainComment(
						report.getKnowledgeScore()))
				.detailCommentInfo(commentGenerator.makeKnowledgeDetailComment(
						report.getKnowledgeCommonScore(), 
						report.getKnowledgeTypeScore(), 
						report.getKnowledgeChangeScore(), 
						report.getKnowledgeSellScore()))
				.problemCorrectInfo(problemCorrectInfo)
				.problemHighLevelInfo(problemHighLevelInfo)
				.problemMiddleLevelInfo(problemMiddleLevelInfo)
				.problemLowLevelInfo(problemLowLevelInfo)
				.build();
		
		return result;
	}
	
	private DiagnosisRecordDetailDTO makeRiskRecordDetail(
			DiagnosisReport report)
	{
		DiagnosisRecordDetailDTO result = DiagnosisRecordDetailDTO.builder()
				.score(report.getRiskScore())
				.percentage(sndCalculator.calculateForDiagnosis(
						sndCalculator.RISK_IDX, 
						report.getRiskScore()))
				.mainCommentInfo(commentGenerator.makeRiskMainComment(
						report.getRiskProfileScore(), 
						report.getRiskTracingScore()))
				.detailCommentInfo(commentGenerator.makeRiskDetailComment(
						report.getRiskProfileScore(), 
						report.getRiskTracingScore()))
				.build();
		
		return result;
	}
	
	private DiagnosisRecordDetailDTO makeInvestRecordDetail(
			DiagnosisReport report)
	{
		DiagnosisRecordDetailDTO result = DiagnosisRecordDetailDTO.builder()
				.score(report.getInvestScore())
				.percentage(sndCalculator.calculateForDiagnosis(
						sndCalculator.INVEST_IDX, 
						report.getInvestScore()))
				.mainCommentInfo(commentGenerator.makeInvestMainComment(
						report.getInvestProfileScore(), 
						report.getInvestTracingScore()))
				.detailCommentInfo(commentGenerator.makeInvestDetailComment(
						report.getInvestProfileScore(), 
						report.getInvestTracingScore()))
				.build();
		
		return result;
	}
	

	// LRS에서 푼 문제 정보를 모아, 관련 정보 생성. (난이도별 문제 맞은 갯수, 해당 문제 정보 등)
	private Map<String, Object> setProbInfoInRecordDTO(List<StatementDTO> infos) throws Exception
	{

		Map<String, Object> result = new HashMap<>();
		List<List<String>> probHighLevelInfo = new ArrayList<>();
		List<List<String>> probMidLevelInfo = new ArrayList<>();
		List<List<String>> probLowLevelInfo = new ArrayList<>();
		Map<String, Object> problemCorrectInfo = new HashMap<>();
		
		// probId, isCorr
		Map<Integer, Integer> probCorrInfo = new HashMap<>();
		List<Integer> probIds = new ArrayList<>();

		for (StatementDTO info : infos) {
			if (info.getSourceType().equals("diagnosis")) {
				try {
					int probId = Integer.parseInt(info.getSourceId());
					probCorrInfo.put(probId, info.getIsCorrect());
				} catch (Exception e) {
					log.info("Integer decode fail in setProbInfoInRecordDTO " + info.getSourceId());
				}
			}
		}

		// 왜 안되냐 ㅡㅡ;;
		List<Problem> diagProbs = problemRepo.findAllById(probCorrInfo.keySet());
		int[] diffCorr = {0, 0, 0};
		int[] diffAll = {0, 0, 0};
		int problemIdx = 1;
		
		for(Problem prob : diagProbs)
		{
			String[] diffList = {"상", "중", "하"};
			int diffIdx = -1;
			int isCorr = probCorrInfo.get(prob.getProbID());
			
			for(int i = 0; i < diffList.length; i ++)
				if(prob.getDifficulty().equals(diffList[i]))
					diffIdx = i;
			
			if(diffIdx < 0 || diffIdx >= diffAll.length)
			{
				log.error("Prob Diffcult error in setProbInfoInRecordDTO. probID : "
						+prob.getProbID()+" "+prob.getDifficulty());
				continue;
			}
			
			if(isCorr == 1)
				diffCorr[diffIdx]++;
			diffAll[diffIdx]++;
			
			List<String> probInfo = new ArrayList<>();
			
			probInfo.add(String.valueOf(problemIdx));
			probInfo.add(String.valueOf(prob.getProbID()));
			probInfo.add((isCorr==1)?"true":"false");
			probInfo.add(prob.getQuestion());
			probInfo.add(prob.getDifficulty());
			
			switch(diffIdx)
			{
			case 0:
				probHighLevelInfo.add(probInfo);
				break;
			case 1:
				probMidLevelInfo.add(probInfo);
				break;
			case 2:
				probLowLevelInfo.add(probInfo);
				break;
			default:
				log.error("Prob Diffcult error in setProbInfoInRecordDTO. probID : "
						+prob.getProbID()+" "+prob.getDifficulty());
				break;
			}
			
			problemIdx++;
		}
		
		problemCorrectInfo.put("allCorr", diffCorr[0]+diffCorr[1]+diffCorr[2]);
		problemCorrectInfo.put("allProb", diffAll[0]+diffAll[1]+diffAll[2]);
		problemCorrectInfo.put("high", diffCorr[0]+"/"+diffAll[0]);
		problemCorrectInfo.put("middle", diffCorr[1]+"/"+diffAll[1]);
		problemCorrectInfo.put("low", diffCorr[2]+"/"+diffAll[2]);
		
		result.put("problemCorrectInfo", problemCorrectInfo);
		result.put("problemHighLevelInfo", probHighLevelInfo);
		result.put("problemMiddleLevelInfo", probMidLevelInfo);
		result.put("problemLowLevelInfo", probLowLevelInfo);
		
		return result;
		
	}

	private List<StatementDTO> getDiagnosisKnowledgeProbInfo(String userId, String probSetId) 
			throws Exception {

		GetStatementInfoDTO input = new GetStatementInfoDTO();
		input.pushUserId(userId);
		if (probSetId != null)
			input.pushExtensionStr(probSetId);
		input.pushActionType("submit");
		input.pushSourceType("diagnosis");
		
		List<StatementDTO> result = new ArrayList<>();
		List<StatementDTO> stateResult;
		Map<String, Integer> isIDExist = new HashMap<>();
		
		try
		{
			stateResult = lrsAPIManager.getStatementList(input);
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
		
		log.info(result.size()+"    "+result.toString());
		

		return result;

	}
}
