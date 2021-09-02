package com.tmax.eTest.Report.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Report.dto.DiagnosisRecordMainDTO;
import com.tmax.eTest.Report.dto.RecommendVideoDTO;
import com.tmax.eTest.Report.exception.ReportBadRequestException;
import com.tmax.eTest.Report.util.SNDCalculator;
import com.tmax.eTest.Report.util.DiagnosisComment;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.report.DiagnosisReportKey;
import com.tmax.eTest.Common.model.video.Video;
import com.tmax.eTest.Common.model.video.VideoBookmarkId;
import com.tmax.eTest.Common.repository.report.DiagnosisReportRepo;
import com.tmax.eTest.Common.repository.user.UserMasterRepo;
import com.tmax.eTest.Common.repository.video.VideoBookmarkRepository;
import com.tmax.eTest.Common.repository.video.VideoRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DiagnosisMainRecordService {

	@Autowired
	LRSAPIManager lrsAPIManager;

	@Autowired
	DiagnosisReportRepo diagnosisReportRepo;
	@Autowired
	UserMasterRepo userMasterRepo;
	@Autowired
	VideoRepository videoRepo;
	@Autowired
	VideoBookmarkRepository videoBookmarkRepo;

	@Autowired
	DiagnosisComment commentGenerator;
	@Autowired
	SNDCalculator sndCalculator;


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
				List<StatementDTO> statements = getDiagnosisInfo(userId, probSetId);
				
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
				
				String nickName = "";
				
				try{
					nickName = userMasterRepo.getById(userId).getNickname();
				}
				catch(Exception e){
					throw new ReportBadRequestException("userId unavailable in getDiagnosisRecordMain. "+userId, e);
				}
				
				result.pushInfoByReport(
						report, 
						percentList, 
						getRecommendVideoMap(userId, report),
						similarTypeInfo,
						getIsAlarm(statements),
						nickName);
			}
			else
				throw new ReportBadRequestException("Problem Set Id is unavailable. "+probSetId);
		}

		return result;
	}
	
	
	private Map<String, List<RecommendVideoDTO>> getRecommendVideoMap(String userId, DiagnosisReport report) throws Exception
	{
		Map<String, List<RecommendVideoDTO>> result = new HashMap<>();
		
		JsonArray basicJsonArr = null, advJsonArr = null, typeJsonArr = null;
		List<RecommendVideoDTO> basicList = null, advList = null, typeList = null;
		
		try
		{
			if(!report.getRecommendBasicList().isEmpty())
				basicJsonArr = JsonParser.parseString(report.getRecommendBasicList()).getAsJsonArray();
			
			if(!report.getRecommendAdvancedList().isEmpty())
				advJsonArr = JsonParser.parseString(report.getRecommendAdvancedList()).getAsJsonArray();
			
			if(!report.getRecommendTypeList().isEmpty())
				typeJsonArr = JsonParser.parseString(report.getRecommendTypeList()).getAsJsonArray();
		}
		catch(Exception e)
		{
			
		}
		
		if(basicJsonArr != null)
			basicList = getRecommendVideoList(userId, basicJsonArr);
		if(basicJsonArr != null)
			advList = getRecommendVideoList(userId, advJsonArr);
		if(basicJsonArr != null)
			typeList = getRecommendVideoList(userId, typeJsonArr);
		
		result.put("basic", basicList);
		result.put("advanced", advList);
		result.put("type", typeList);
		
		return result;
	}
	
	private List<RecommendVideoDTO> getRecommendVideoList(String userId, JsonArray recInfoJsonArray)
	{
		List<RecommendVideoDTO> result = new ArrayList<>();
		
		for(int jsonArrIdx = 0; jsonArrIdx < recInfoJsonArray.size(); jsonArrIdx++)
		{
			JsonObject recObj = recInfoJsonArray.get(jsonArrIdx).getAsJsonObject();
			Long recVideoId = Long.parseLong(recObj.get("id").getAsString());
			
			Video videoInfo = null;
			
			boolean isBookmark = false;
			int hit = 0;
			
			try {
				videoInfo = videoRepo.getById(recVideoId);
				hit = videoInfo.getVideoHit().getHit();
			}
			catch(Exception e)
			{
				log.info("VideoId unavailable in getRecommendVideoList - "+recVideoId);
				continue;
			}
			
			try {
				VideoBookmarkId bookmarkId = new VideoBookmarkId(userId, recVideoId);
				videoBookmarkRepo.getById(bookmarkId);
				isBookmark = true;
			}
			catch(Exception e)
			{
				// find bookmark fail do nothing.
			}
						
			result.add(new RecommendVideoDTO(videoInfo, hit, isBookmark));
		}
		
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
				
				if(extObj.get("guessAlarm").getAsInt() == 1)
					checkAlarm++;
			}
			catch(Exception e)
			{
				log.info("Statement Extension Json Parse Fail in getIsAlarm - "+extJsonStr);
			}
			
			
		}
		
		if(checkAlarm >= 3)
			result = true;
		
		return result;
	}
	
	
	
	private List<StatementDTO> getDiagnosisInfo(String userId, String probSetId) 
			throws Exception {

		GetStatementInfoDTO input = new GetStatementInfoDTO();
		input.pushUserId(userId);
		if (probSetId != null)
			input.pushExtensionStr(probSetId);
		input.pushActionType("submit");
		input.pushSourceType("diagnosis");
		input.pushSourceType("diagnosis_pattern");
		
		List<StatementDTO> result = new ArrayList<>();
		
		try
		{
			result = lrsAPIManager.getStatementList(input);
		}
		catch(Exception e)
		{
			throw new ReportBadRequestException("Exception in Diagnosis Report, get statement part.", e);
		}
		
		log.info(result.size()+"    "+result.toString());
		

		return result;

	}
	

}
