package com.tmax.eTest.Report.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.video.Video;
import com.tmax.eTest.Common.model.video.VideoBookmark;
import com.tmax.eTest.Common.model.video.VideoBookmarkId;
import com.tmax.eTest.Common.repository.user.UserKnowledgeRepo;
import com.tmax.eTest.Common.repository.video.VideoBookmarkRepository;
import com.tmax.eTest.Common.repository.video.VideoRepository;
import com.tmax.eTest.Common.repository.video.VideoUkRelRepository;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Report.dto.RecommendVideoDTO;
import com.tmax.eTest.Report.exception.ReportBadRequestException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DiagnosisVideoService {

	@Autowired
	VideoBookmarkRepository videoBookmarkRepo;
	
	@Autowired
	UserKnowledgeRepo userKnowledgeRepo;
	
	@Autowired
	VideoUkRelRepository videoUkRelRepo;
	
	@Autowired
	VideoRepository videoRepo;
	
	@Autowired
	LRSAPIManager lrsManager;

	public boolean setVideoBookmark(String userId, String videoId, boolean isCheckBookmark) throws Exception {
		
		VideoBookmarkId videoBookmarkId = new VideoBookmarkId(userId, videoId);
		VideoBookmark videoBookmark = new VideoBookmark(userId, videoId);

		if(isCheckBookmark)
		{
			if (videoBookmarkRepo.existsById(videoBookmarkId))
				throw new ReportBadRequestException("VideoBookmark already exists in VideoBookmark Table");
			else
				videoBookmarkRepo.save(videoBookmark);
		}
		else
		{
		    videoBookmarkRepo.delete(videoBookmarkRepo.findById(videoBookmarkId).orElseThrow(
		        () -> new ReportBadRequestException("VideoBookmark doesn't exist in VideoBookmark Table")));

		}

		return true;
	}
	
	

	private Map<String, List<RecommendVideoDTO>> getRecommendVideoMap(String userId, DiagnosisReport report) 
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
		catch(JsonParseException e)
		{
			log.info("Json Parse Fail in getRecommendVideoMap. : " 
				+ report.getRecommendBasicList()
				+" "+report.getRecommendAdvancedList()
				+" "+report.getRecommendTypeList());
		}
		catch(IllegalStateException e)
		{
			log.info("Get Json Array Fail in getRecommendVideoMap. : "	
				+ report.getRecommendBasicList()
				+" "+report.getRecommendAdvancedList()
				+" "+report.getRecommendTypeList());
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
			String recVideoId = recObj.get("id").getAsString();
			
			Optional<Video> videoInfoOpt = null;
			
			boolean isBookmark = false;
			int hit = 0;
			
		
			videoInfoOpt = videoRepo.findById(recVideoId);
			
			if(videoInfoOpt.isPresent()) {				
				if(videoInfoOpt.get().getVideoHit() != null)
					hit = videoInfoOpt.get().getVideoHit().getHit();
			}
			else
			{
				log.info("VideoId unavailable in getRecommendVideoList - "+recVideoId);
				continue;
			}
			
			
			try {
				VideoBookmarkId bookmarkId = new VideoBookmarkId(userId, recVideoId);
				isBookmark = videoBookmarkRepo.existsById(bookmarkId);
			}
			catch(IllegalArgumentException e)
			{
				// find bookmark fail do nothing.
				log.info("find bookmarkFail");
			}
					
			if(videoInfoOpt.isPresent())
				result.add(new RecommendVideoDTO(videoInfoOpt.get(), hit, isBookmark));
			else
				log.info("Can not find Video => ID : "+recVideoId);
		}
		
		return result;
	}

}
