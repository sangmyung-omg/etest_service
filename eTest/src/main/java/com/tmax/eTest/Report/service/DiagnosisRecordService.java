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
import com.tmax.eTest.Report.dto.DiagnosisRecordDTO;
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
	
	public DiagnosisRecordDTO getDiagnosisRecord(
			String userId, 
			String probSetId) throws Exception
	{
		DiagnosisRecordDTO result = new DiagnosisRecordDTO();
		
		List<StatementDTO> diagStatement = getDiagnosisProbInfo(userId, null);
		lrsAPIManager.saveStatementList(diagStatement);
		
		// 1. 해당 유저의 문제 푼 정보 수집.
		
//		setProbInfoInRecordDTO(result, diagStatement);
//		
		result.initForDummy();
		
		return result;
	}
	
	// LRS에서 푼 문제 정보를 모아, 관련 정보 생성. (난이도별 문제 맞은 갯수, 해당 문제 정보 등)
	private void setProbInfoInRecordDTO(DiagnosisRecordDTO output, List<StatementDTO> infos)
	{
		
		// probId, isCorr
		Map<Integer, Integer> probCorrInfo = new HashMap<>();
		
		for(StatementDTO info : infos)
		{
			if(info.getSourceType().equals("diagnosis"))
			{
				try
				{
					int probId = Integer.getInteger(info.getSourceId());
					probCorrInfo.put(probId, info.getIsCorrect());
					
				}
				catch(Exception e)
				{
					logger.info("Integer decode fail in setProbInfoInRecordDTO "+info.toString());
				}
			}
		}
		
		List<Problem> diagProbs = problemRepo.findAllById(probCorrInfo.keySet());
		output.pushProblemList(diagProbs, probCorrInfo);
		
	}
	
	private List<StatementDTO> getDiagnosisProbInfo(String userId, String probSetId) throws Exception
	{
		
		GetStatementInfoDTO input = new GetStatementInfoDTO();
		input.pushUserId(userId);
		if(probSetId != null)
			input.pushExtensionStr(probSetId);
		input.pushActionType("submit");
		List<StatementDTO> result = lrsAPIManager.getStatementList(input);
		
		return result;
		
	}
}
