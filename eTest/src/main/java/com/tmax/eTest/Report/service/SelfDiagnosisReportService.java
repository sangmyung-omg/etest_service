 package com.tmax.eTest.Report.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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
		List<StatementDTO> diagnosisProbStatements = getStatementDiagnosisProb(id);
		List<Pair<Problem, Integer>> probInfos = getProblemAndChoiceInfos(diagnosisProbStatements);
		Map<Integer, UkMaster> usedUkMap = stateAndProbProcess.makeUsedUkMapWithPair(probInfos);
		
		result.initForDummy();
		
		probDivideAndCalculateScores(probInfos);
		
		return result;
	}
	
	public Map<String,Integer> probDivideAndCalculateScores(List<Pair<Problem, Integer>> probInfos)
	{
		Map<String,Integer> res = new HashMap<>();
		List<Pair<Problem, Integer>> investCondProb = new ArrayList<>(); // 투자현황
		List<Pair<Problem, Integer>> riskProb = new ArrayList<>(); // 리스크
		List<Pair<Problem, Integer>> investRuleProb = new ArrayList<>(); // 투자원칙
		List<Pair<Problem, Integer>> cogBiasProb = new ArrayList<>(); // 인지편향
		List<Pair<Problem, Integer>> investKnowledgeProb = new ArrayList<>(); // 투자지식

		
		for(Pair<Problem, Integer> probInfo : probInfos)
		{
			Problem prob = probInfo.getFirst();
			
			if(prob.getDiagnosisInfo() != null && prob.getDiagnosisInfo().getCurriculum() != null)
			{
				String section = prob.getDiagnosisInfo().getCurriculum().getSection();
				
				switch(section)
				{
				case "투자현황":
					investCondProb.add(probInfo);
					break;
				case "리스크":
					riskProb.add(probInfo);
					break;
				case "투자원칙":
					investRuleProb.add(probInfo);
					break;
				case "인지편향":
					cogBiasProb.add(probInfo);
					break;
				case "투자지식":
					investKnowledgeProb.add(probInfo); 
					break;
				default:
					logger.info("probDivideAndCalculateScores section invalid : " + section);
					break;
				}
			}
			else
			{
				logger.info("probDivideAndCalculateScores prob not have diagnosisInfo : " + prob.toString());
			}
		}
		
		
		return res;
	}
	
	public PartUnderstandingDTO getPartInfo(String id, String partName)
	{
		PartUnderstandingDTO res = new PartUnderstandingDTO();
		
		return res;
	}
	
	private List<Pair<Problem, Integer>> getProblemAndChoiceInfos(List<StatementDTO> statementList)
	{
		List<Pair<Problem, Integer>> result = new ArrayList<>();
		List<Problem> probList = getProblemInfos(statementList);
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
