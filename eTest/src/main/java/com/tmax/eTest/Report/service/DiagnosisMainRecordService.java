package com.tmax.eTest.Report.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.Report.dto.DiagnosisRecordMainDTO;
import com.tmax.eTest.Report.exception.ReportBadRequestException;
import com.tmax.eTest.Report.util.SNDCalculator;
import com.tmax.eTest.Report.util.StateAndProbProcess;
import com.tmax.eTest.Report.util.diagnosis.DiagnosisComment;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.user.UserMaster;
import com.tmax.eTest.Common.repository.report.DiagnosisReportRepo;
import com.tmax.eTest.Common.repository.user.UserMasterRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DiagnosisMainRecordService {

	@Autowired
	DiagnosisReportRepo diagnosisReportRepo;
	@Autowired
	UserMasterRepo userMasterRepo;
	
	@Autowired
	StateAndProbProcess probProcessor;
	@Autowired
	DiagnosisComment commentGenerator;
	@Autowired
	SNDCalculator sndCalculator;


	public DiagnosisRecordMainDTO getDiagnosisRecordMain(
			String id, 		// 회원인 경우 ID 가져옴. 비회원일 경우 Null
			String probSetId)  throws ParseException{
		
		DiagnosisRecordMainDTO result = new DiagnosisRecordMainDTO();

		if(probSetId.equals("dummy"))
			result.initForDummy();
		else
		{			
			Optional<DiagnosisReport> reportOpt = diagnosisReportRepo.findById(probSetId);
			
			// report 유효 검사
			if(reportOpt.isPresent())
			{
				String userId = id;
				DiagnosisReport report = reportOpt.get();
				List<StatementDTO> knowledgeStatements = probProcessor.getDiagnosisKnowledgeProbInfo(userId, probSetId);
				
				// 
				if(knowledgeStatements.size() < 0 && id == null)
					throw new ReportBadRequestException("Nonmember's probSetId not available in getDiagnosisRecordMain. " + probSetId);
				else
					userId = knowledgeStatements.get(0).getUserId();
				
				Map<String, Integer> percentList = new HashMap<>();
				percentList.put("gi", 
					sndCalculator.calculatePercentage(SNDCalculator.Type.DIAG_GI ,report.getGiScore()));
				percentList.put("risk",
					sndCalculator.calculatePercentage(SNDCalculator.Type.DIAG_RISK, report.getRiskScore()));
				percentList.put("invest", 
					sndCalculator.calculatePercentage(SNDCalculator.Type.DIAG_INVEST, report.getInvestScore()));
				percentList.put("knowledge", 
					sndCalculator.calculatePercentage(SNDCalculator.Type.DIAG_KNOWLEDGE, report.getKnowledgeScore()));
				
				String nickName = "비회원";
				
				
				// 회원 ID 유효 검사 및 회원 닉네임 가져오기.
				// 비회원인 경우 (id == null 인 경우) Pass
				if(id != null)
				{
					UserMaster userMaster = userMasterRepo.getById(userId);
					
					if(userMaster != null)
						nickName = userMaster.getNickname();
					else
						throw new ReportBadRequestException("userId unavailable in getDiagnosisRecordMain. "+userId);
				}
		
				
				result.pushInfoByReport(
						report, 
						percentList, 
						getCommentInfo(report),
						probProcessor.getProbInfoInRecordDTO(knowledgeStatements),
						getIsAlarm(knowledgeStatements),
						nickName);
			}
			else
				throw new ReportBadRequestException("Problem Set Id is unavailable. "+probSetId);
		}

		return result;
	}

	
	private Map<String, String> getCommentInfo(DiagnosisReport report)
	{
		Map<String, String> result = new HashMap<>();
		
		Map<String, String> riskMainComment = commentGenerator.makeRiskMainComment(
				report);
		Map<String, String> investMainComment = commentGenerator.makeInvestMainComment(
				report.getInvestProfileScore(), 
				report.getInvestTracingScore());
		Map<String, String> knowledgeMainComment = commentGenerator.makeKnowledgeMainComment(
				report.getKnowledgeScore(),
				null,
				null);
		
		result.put("risk", riskMainComment.get("main"));
		result.put("invest", investMainComment.get("main"));
		result.put("knowledge", knowledgeMainComment.get("main"));
		
		return result;
	}
	
	
	private boolean getIsAlarm(List<StatementDTO> diagStatements)
	{
		boolean result = false;
		
		int checkAlarm = 0;
		
		for(StatementDTO state : diagStatements)
		{
			String extJsonStr = state.getExtension();
			
			if(extJsonStr == null || extJsonStr.isEmpty())
				continue;
			
			try
			{
				JsonObject extObj = JsonParser.parseString(extJsonStr).getAsJsonObject();
				
				if(extObj.get("guessAlarm").getAsBoolean())
					checkAlarm++;
			}
			catch(ClassCastException e)
			{
				log.info("Statement Extension Json Parse Fail in getIsAlarm - "+extJsonStr);
			}
			catch(IllegalStateException e)
			{
				log.info("Statement Extension Json Parse Fail in getIsAlarm - "+extJsonStr);
			}
			catch(JsonParseException e)
			{
				log.info("Statement Extension Json Parse Fail in getIsAlarm - "+extJsonStr);
			}
			
		}
		
		if(checkAlarm >= 3)
			result = true;
		
		return result;
	}	

}
