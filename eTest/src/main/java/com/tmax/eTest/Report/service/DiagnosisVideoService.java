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

	public boolean setVideoBookmark(String userId, String videoId, boolean isCheckBookmark) throws Exception
	{
		try
		{
			if(isCheckBookmark)
			{
				VideoBookmark bookmarkModel = new VideoBookmark(userId, videoId);
				videoBookmarkRepo.save(bookmarkModel);
			}
			else
			{
				VideoBookmarkId bookmarkId = new VideoBookmarkId(userId, videoId);
				videoBookmarkRepo.deleteById(bookmarkId);
			}
		}
		catch(Exception e)
		{
			if(isCheckBookmark)
				throw new ReportBadRequestException("Save Video Bookmark error. Check userID & videoID.", e);
			else
				throw new ReportBadRequestException("Delete Video Bookmark error. Check userID & videoID.", e);
		}

		return true;
	}
}
