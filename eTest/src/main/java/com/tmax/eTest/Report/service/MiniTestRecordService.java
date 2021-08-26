package com.tmax.eTest.Report.service;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Report.dto.MiniTestRecordDTO;
import com.tmax.eTest.Report.util.RuleBaseScoreCalculator;
import com.tmax.eTest.Report.util.SNDCalculator;
import com.tmax.eTest.Report.util.SelfDiagnosisComment;
import com.tmax.eTest.Report.util.StateAndProbProcess;
import com.tmax.eTest.Report.util.TritonAPIManager;
import com.tmax.eTest.Report.util.UKScoreCalculator;
import com.tmax.eTest.Common.repository.report.DiagnosisReportRepo;
import com.tmax.eTest.Test.repository.UserKnowledgeRepository;

@Service
public class MiniTestRecordService {

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
	
	public MiniTestRecordDTO getMiniTestRecord(
			String userId, 
			String probSetId) throws Exception
	{
		MiniTestRecordDTO result = new MiniTestRecordDTO();
		
		result.initForDummy();
		
		return result;
	}
	
}
