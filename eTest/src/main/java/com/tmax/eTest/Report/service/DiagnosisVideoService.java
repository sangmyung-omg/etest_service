package com.tmax.eTest.Report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Common.model.video.VideoBookmark;
import com.tmax.eTest.Common.model.video.VideoBookmarkId;
import com.tmax.eTest.Common.repository.user.UserKnowledgeRepo;
import com.tmax.eTest.Common.repository.video.VideoBookmarkRepository;
import com.tmax.eTest.Common.repository.video.VideoRepository;
import com.tmax.eTest.Common.repository.video.VideoUkRelRepository;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Report.exception.ReportBadRequestException;

import lombok.extern.log4j.Log4j2;

@Log4j2
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
	
	

}
