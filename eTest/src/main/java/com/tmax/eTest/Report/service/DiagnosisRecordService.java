package com.tmax.eTest.Report.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.tmax.eTest.Report.dto.DiagnosisRecordDetailDTO;
import com.tmax.eTest.Report.dto.DiagnosisRecordMainDTO;
import com.tmax.eTest.Report.dto.PartUnderstandingDTO;
import com.tmax.eTest.Report.dto.triton.TritonDataDTO;
import com.tmax.eTest.Report.dto.triton.TritonResponseDTO;
import com.tmax.eTest.Report.exception.ReportBadRequestException;
import com.tmax.eTest.Report.util.RuleBaseScoreCalculator;
import com.tmax.eTest.Report.util.SNDCalculator;
import com.tmax.eTest.Report.util.DiagnosisComment;
import com.tmax.eTest.Report.util.StateAndProbProcess;
import com.tmax.eTest.Report.util.TritonAPIManager;
import com.tmax.eTest.Report.util.UKScoreCalculator;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.report.DiagnosisReportKey;
import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.Common.repository.report.DiagnosisReportRepo;
import com.tmax.eTest.Test.repository.UserKnowledgeRepository;

@Service
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

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

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
					result = makeKnowledgeRecordDetail(report);
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
	
	private DiagnosisRecordDetailDTO makeKnowledgeRecordDetail(
			DiagnosisReport report)
	{
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
//	private void setProbInfoInRecordDTO(DiagnosisRecordMainDTO output, List<StatementDTO> infos) {
//
//		// probId, isCorr
//		Map<Integer, Integer> probCorrInfo = new HashMap<>();
//		List<Integer> probIds = new ArrayList<>();
//
//		for (StatementDTO info : infos) {
//			if (info.getSourceType().equals("diagnosis")) {
//				try {
//					int probId = Integer.parseInt(info.getSourceId());
//					probCorrInfo.put(probId, info.getIsCorrect());
//				} catch (Exception e) {
//					logger.info("Integer decode fail in setProbInfoInRecordDTO " + info.getSourceId());
//				}
//			}
//		}
//
//		// 왜 안되냐 ㅡㅡ;;
//		List<Problem> diagProbs = problemRepo.findAllById(probCorrInfo.keySet());
//		logger.info(diagProbs.size() + " "+probCorrInfo.keySet().toString());
//		for (Problem prob : diagProbs)
//			logger.info(prob.getProbID() + "");
//		//output.pushProblemList(diagProbs, probCorrInfo);
//		logger.info(probIds.toString());
//	}

	private List<StatementDTO> getDiagnosisProbInfo(String userId, String probSetId) throws Exception {

		GetStatementInfoDTO input = new GetStatementInfoDTO();
		input.pushUserId(userId);
		if (probSetId != null)
			input.pushExtensionStr(probSetId);
		input.pushActionType("submit");
		List<StatementDTO> result = lrsAPIManager.getStatementList(input);

		return result;

	}
}
