package com.tmax.eTest.Report.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.repository.report.DiagnosisReportRepo;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Report.dto.DiagnosisRecordDetailDTO;
import com.tmax.eTest.Report.exception.ReportBadRequestException;
import com.tmax.eTest.Report.util.DiagnosisComment;
import com.tmax.eTest.Report.util.SNDCalculator;
import com.tmax.eTest.Report.util.StateAndProbProcess;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DiagnosisDetailRecordService {
	
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
	
	@Autowired
	StateAndProbProcess probProcessor;


	private final List<String> partNameList = new ArrayList<>(Arrays.asList("risk", "invest", "knowledge"));

	public DiagnosisRecordDetailDTO getDiagnosisRecordDetail(
			String id, 
			String probSetId,
			String partName) throws Exception {
		DiagnosisRecordDetailDTO result = new DiagnosisRecordDetailDTO();
		
		if(!partNameList.contains(partName))
			throw new ReportBadRequestException("PartName unavailable. "+partName);
		
		if(probSetId.equals("dummy"))
			result.initForDummy();
		else
		{
			Optional<DiagnosisReport> reportOpt = diagnosisReportRepo.findById(probSetId);
			
			
			
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
					List<StatementDTO> knowledgeProbStatement = probProcessor.getDiagnosisKnowledgeProbInfo(probSetId);
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
		List<Integer> probIdList = new ArrayList<>();
		
		for(StatementDTO state : knowledgeProbStatement)
			probIdList.add(Integer.parseInt(state.getSourceId()));
		
		List<Problem> probList = problemRepo.findAllById(probIdList);		
		
		DiagnosisRecordDetailDTO result = DiagnosisRecordDetailDTO.builder()
				.score(report.getKnowledgeScore())
				.percentage(sndCalculator.calculatePercentage(
						SNDCalculator.Type.DIAG_KNOWLEDGE, 
						report.getKnowledgeScore()))
				.mainCommentInfo(commentGenerator.makeKnowledgeMainComment(
						report.getKnowledgeScore(), probList, knowledgeProbStatement))
				.detailCommentInfo(commentGenerator.makeKnowledgeDetailComment(report))
				.build();
		log.info(result.toString());
		return result;
	}
	
	private DiagnosisRecordDetailDTO makeRiskRecordDetail(
			DiagnosisReport report)
	{
		DiagnosisRecordDetailDTO result = DiagnosisRecordDetailDTO.builder()
				.score(report.getRiskScore())
				.percentage(sndCalculator.calculatePercentage(
						SNDCalculator.Type.DIAG_RISK, 
						report.getRiskScore()))
				.mainCommentInfo(commentGenerator.makeRiskMainComment(
						report))
				.detailCommentInfo(commentGenerator.makeRiskDetailComment(
						report))
				.build();
		
		return result;
	}
	
	private DiagnosisRecordDetailDTO makeInvestRecordDetail(
			DiagnosisReport report)
	{
		DiagnosisRecordDetailDTO result = DiagnosisRecordDetailDTO.builder()
				.score(report.getInvestScore())
				.percentage(sndCalculator.calculatePercentage(
						SNDCalculator.Type.DIAG_INVEST, 
						report.getInvestScore()))
				.mainCommentInfo(commentGenerator.makeInvestMainComment(
						report.getInvestProfileScore(), 
						report.getInvestTracingScore()))
				.detailCommentInfo(commentGenerator.makeInvestDetailComment(
						report))
				.build();
		
		return result;
	}

}
