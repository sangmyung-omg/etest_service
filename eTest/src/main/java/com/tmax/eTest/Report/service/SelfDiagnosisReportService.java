 package com.tmax.eTest.Report.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Contents.model.Problem;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.Report.dto.DiagnosisResultDTO;
import com.tmax.eTest.Report.dto.PartUnderstandingDTO;
import com.tmax.eTest.Report.dto.lrs.GetStatementInfoDTO;
import com.tmax.eTest.Report.dto.lrs.StatementDTO;
import com.tmax.eTest.Report.dto.triton.TritonRequestDTO;
import com.tmax.eTest.Report.util.LRSAPIManager;
import com.tmax.eTest.Report.util.StateAndProbProcess;
import com.tmax.eTest.Report.util.TritonAPIManager;
import com.tmax.eTest.Test.model.UkMaster;
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
	StateAndProbProcess stateAndProbProcess;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	
	public DiagnosisResultDTO calculateDiagnosisResult(String id)
	{
		DiagnosisResultDTO result = new DiagnosisResultDTO();
		List<StatementDTO> tracingProbStatements = getStatementInTracingProb(id);
		List<Problem> tracingProbInfos = getProblemInfos(tracingProbStatements);
		List<StatementDTO> profileProbStatement = getStatementInProfileProb(id);
		List<Problem> profileProbInfos = getProblemInfos(profileProbStatement);
		Map<Integer, UkMaster> usedUkMap = stateAndProbProcess.makeUsedUkMap(profileProbInfos);
		
		
		result.initForDummy();
		
		return result;
	}
	
	public PartUnderstandingDTO getPartInfo(String id, String partName)
	{
		PartUnderstandingDTO res = new PartUnderstandingDTO();
		
		return res;
	}
	
	private List<Problem> getProblemInfos(List<StatementDTO> miniTestResult) {
		List<Integer> probIdList = new ArrayList<>();
		List<Problem> probList = new ArrayList<>();

		for (StatementDTO dto : miniTestResult) {
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
	
	private List<StatementDTO> getStatementInProfileProb(String id)
	{
		GetStatementInfoDTO getStateInfo = new GetStatementInfoDTO();
	
		getStateInfo.pushUserId(id);

		getStateInfo.pushSourceType("diagnosis");		
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
	
	private List<StatementDTO> getStatementInTracingProb(String id)
	{
		GetStatementInfoDTO getStateInfo = new GetStatementInfoDTO();
	
		getStateInfo.pushUserId(id);

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
