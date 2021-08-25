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
import com.tmax.eTest.Common.model.report.DiagnosisReportKey;
import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.Common.repository.report.DiagnosisReportRepo;
import com.tmax.eTest.Test.repository.UserKnowledgeRepository;

@Service
public class DiagnosisRecordService {

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

	private final List<String> partNameList = new ArrayList<>(Arrays.asList("risk", "invest", "knowledge"));


	public DiagnosisRecordMainDTO getDiagnosisRecordMain(String userId, String probSetId) throws Exception {
		DiagnosisRecordMainDTO result = new DiagnosisRecordMainDTO();

//		DiagnosisReportKey key = new DiagnosisReportKey();
//		key.setDiagnosisId(probSetId);
//		
//		Optional<DiagnosisReport> report = diagnosisReportRepo.findById(key);
//		
//		if(report.isPresent())
//		{
//			
//		}
//		else
//			throw new ReportBadRequestException("Problem Set Id is unavailable. "+probSetId);

		result.initForDummy();

		return result;
	}
	
	public DiagnosisRecordDetailDTO getDiagnosisRecordDetail(
			String userId, 
			String probSetId,
			String partName) throws Exception {
		DiagnosisRecordDetailDTO result = new DiagnosisRecordDetailDTO();
		
		if(!partNameList.contains(partName))
			throw new ReportBadRequestException("PartName unavailable. "+partName);

//		DiagnosisReportKey key = new DiagnosisReportKey();
//		key.setDiagnosisId(probSetId);
//		
//		Optional<DiagnosisReport> report = diagnosisReportRepo.findById(key);
//		
//		if(report.isPresent())
//		{
//			
//		}
//		else
//			throw new ReportBadRequestException("Problem Set Id is unavailable. "+probSetId);
		
		

		result.initForDummy();

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
